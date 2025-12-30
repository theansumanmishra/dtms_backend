package com.disputetrackingsystem.service;

import com.disputetrackingsystem.eventlistener.DisputeStatusChangedEvent;
import com.disputetrackingsystem.eventlistener.DisputeCreatedEvent;
import com.disputetrackingsystem.eventlistener.DisputeDeleteEvent;
import com.disputetrackingsystem.model.ConfigurableListDetails;
import com.disputetrackingsystem.model.Dispute;
import com.disputetrackingsystem.model.SavingsAccountTransaction;
import com.disputetrackingsystem.repository.ConfigurableListDetailsRepository;
import com.disputetrackingsystem.repository.DisputeRepository;
import com.disputetrackingsystem.repository.SavingsAccountTransactionRepository;
import com.disputetrackingsystem.model.User;
import com.disputetrackingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.lang.NonNull;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DisputeService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DisputeRepository disputeRepository;

    @Autowired
    private SavingsAccountTransactionRepository savingsAccountTransactionRepository;

    @Autowired
    private ConfigurableListDetailsRepository configurableListDetailsRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    // GET STATUS BY NAME & DETAILS
    private ConfigurableListDetails getStatusByName(String listName, String detailsName) {
        return configurableListDetailsRepository.findByListNameAndDetailsName(listName, detailsName);
    }

    private ConfigurableListDetails getsubStatusByName(String listName, String detailsName) {
        return configurableListDetailsRepository.findByListNameAndDetailsName(listName, detailsName);
    }

    // RAISE DISPUTE
    public Dispute createDispute(Dispute dispute) {
        // Get logged-in user from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Set createdBy automatically
        dispute.setCreatedBy(user);
        // Fetch TXN-Id
        Long savingsAccountTransactionId = dispute.getSavingsAccountTransaction().getId();
        // Link TXN-Id
        SavingsAccountTransaction savingsAccountTransaction = savingsAccountTransactionRepository
                .findById(savingsAccountTransactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        dispute.setSavingsAccountTransaction(savingsAccountTransaction);

        // Check if dispute already exists for this transaction
        boolean exists = disputeRepository.existsBySavingsAccountTransaction(savingsAccountTransaction);
        if (exists) {
            throw new IllegalArgumentException(
                    "A dispute already exists for transaction ID: " + savingsAccountTransactionId);
        }

        // Set initial status to INITIATED
        ConfigurableListDetails initiatedStatus = getStatusByName("status", "INITIATED");
        dispute.setStatus(initiatedStatus);
        // Set initial sub-status to PENDING REVIEW
        ConfigurableListDetails initiatedSubStatus = getsubStatusByName("sub_status", "PENDING_REVIEW");
        dispute.setSubStatus(initiatedSubStatus);

        // Save dispute
        Dispute savedDispute = disputeRepository.save(dispute);

        // Mark transaction as disputable
        SavingsAccountTransaction txn = dispute.getSavingsAccountTransaction();
        if (txn != null) {
            txn.setDisputable(true);
            savingsAccountTransactionRepository.save(txn);
        }

        // Publish the event AFTER save
        eventPublisher.publishEvent(new DisputeCreatedEvent(this, savedDispute));

        return savedDispute;
    }

    // SHOW DISPUTE BY ID
    public Dispute getDisputeById(@NonNull Long id) {
        return disputeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispute not found"));
    }

    // SHOW FILTERED DISPUTES(ALL/UNDER-VERIFICATION/UNDER-REVIEWED)
    public Page<Dispute> showAllDispute(Pageable pageable, String filter) {
        PageRequest pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("createdDate").descending());

        if ("pending-review".equalsIgnoreCase(filter)) {
            return disputeRepository.findByReviewedByIsNull(pageRequest);
        } else if ("under-review".equalsIgnoreCase(filter)) {
            return disputeRepository.findBySubStatus_NameIgnoreCase("UNDER_REVIEW", pageRequest);
        } else if ("pending-verification".equalsIgnoreCase(filter)) {
            return disputeRepository.findBySubStatus_NameIgnoreCase("PENDING_VERIFICATION", pageRequest);
        } else {
            return disputeRepository.findAll(pageRequest);
        }
    }

    // SHOW DISPUTES BY ACCOUNT NUMBER
    public Page<Dispute> getDisputesByAccountNumber(long accountNumber, Pageable pageable) {
        return disputeRepository
                .findBySavingsAccountTransaction_SavingsAccount_AccountNumber(accountNumber, pageable);
    }

    // SHOW DISPUTE DASHBOARD
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> dashboardData = new HashMap<>();

        // STATUS WISE COUNT (query using DB uppercase, return nice labels)
        Map<String, Long> subStatusCounts = new HashMap<>();
        subStatusCounts.put("Pending Review", disputeRepository.countBySubStatus_NameIgnoreCase("PENDING_REVIEW"));
        subStatusCounts.put("Under Review", disputeRepository.countBySubStatus_NameIgnoreCase("UNDER_REVIEW"));
        subStatusCounts.put("Accepted", disputeRepository.countBySubStatus_NameIgnoreCase("ACCEPTED"));
        subStatusCounts.put("Rejected", disputeRepository.countBySubStatus_NameIgnoreCase("REJECTED"));
        subStatusCounts.put("Partially Accepted",
                disputeRepository.countBySubStatus_NameIgnoreCase("PARTIALLY_ACCEPTED"));

        // TIME BASED COUNT
        Map<String, Long> timeCounts = new HashMap<>();
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startOfToday = today.atStartOfDay();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate startOfYear = today.withDayOfYear(1);

        Date nowDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        timeCounts.put("Today", disputeRepository.countByCreatedDateBetween(
                Date.from(startOfToday.atZone(ZoneId.systemDefault()).toInstant()), nowDate));

        timeCounts.put("This Week", disputeRepository.countByCreatedDateBetween(
                Date.from(startOfWeek.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), nowDate));

        timeCounts.put("This Month", disputeRepository.countByCreatedDateBetween(
                Date.from(startOfMonth.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), nowDate));

        timeCounts.put("This Year", disputeRepository.countByCreatedDateBetween(
                Date.from(startOfYear.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), nowDate));

        timeCounts.put("Total", disputeRepository.count());

        dashboardData.put("subStatusCounts", subStatusCounts);
        dashboardData.put("timeCounts", timeCounts);

        return dashboardData;
    }

    // DELETE DISPUTE BY ID
    public void deleteDispute(@NonNull Long id) {
        // Fetch dispute
        Dispute dispute = disputeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispute not found"));

        // Reset transaction disputable flag to false
        SavingsAccountTransaction txn = dispute.getSavingsAccountTransaction();
        if (txn != null) {
            txn.setDisputable(false);
            savingsAccountTransactionRepository.save(txn);
        }

        // Delete dispute
        disputeRepository.delete(dispute);

        // Publish delete event
        eventPublisher.publishEvent(new DisputeDeleteEvent(this, dispute));
    }

    // UPDATE DISPUTE
    public Dispute updateDisputeStatusAndSubStatus(@NonNull Long disputeId,
            String newStatusName,
            String newSubStatusName,
            String comments,
            BigDecimal refund,
            Boolean vendorVerified) {
        // 1️⃣ Fetch the dispute
        Dispute dispute = getDisputeById(disputeId);

        // 2️⃣ Fetch new status & sub-status using your existing repository method
        ConfigurableListDetails newStatus = getStatusByName("status", newStatusName);
        ConfigurableListDetails newSubStatus = getStatusByName("sub_status", newSubStatusName);

        if (newStatus == null || newSubStatus == null) {
            throw new RuntimeException("Invalid status or sub-status name provided");
        }

        // 3️⃣ Update fields
        dispute.setStatus(newStatus);
        dispute.setSubStatus(newSubStatus);
        dispute.setComments(comments);
        dispute.setRefund(refund);

        if (vendorVerified != null) {
            dispute.setVendorVerified(vendorVerified); // save checkbox value
        }

        // 4️⃣ Set reviewedBy = logged-in manager
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("No authenticated user found");
        }

        String username = authentication.getName();

        User manager = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        dispute.setReviewedBy(manager);

        // 5️⃣ Save updated dispute
        Dispute savedDispute = disputeRepository.save(dispute);

        // 6️⃣ If dispute is CLOSED, publish email event
        eventPublisher.publishEvent(new DisputeStatusChangedEvent(this, savedDispute));

        return savedDispute;
    }

    // GET DISPUTE CREATE & REVIEW STATS FOR EACH USER
    public Map<String, Long> getDisputeStatsForCurrentUser(Long userId) {
        Object result = disputeRepository.getDisputeStatsForUser(userId);
        Object[] counts = result != null ? (Object[]) result : new Object[] { 0L, 0L };

        Map<String, Long> stats = new HashMap<>();
        stats.put("disputesCreated", counts[0] != null ? ((Number) counts[0]).longValue() : 0L);
        stats.put("disputesReviewed", counts[1] != null ? ((Number) counts[1]).longValue() : 0L);

        return stats;
    }

    // GET RECENT DISPUTES
    public List<Dispute> getRecentDisputes() {
        return disputeRepository.findTop10ByOrderByCreatedDateDesc();
    }
}
