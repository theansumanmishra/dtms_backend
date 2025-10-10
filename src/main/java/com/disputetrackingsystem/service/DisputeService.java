package com.disputetrackingsystem.service;

import com.disputetrackingsystem.model.ConfigurableListDetails;
import com.disputetrackingsystem.model.Dispute;
import com.disputetrackingsystem.model.SavingsAccountTransaction;
import com.disputetrackingsystem.repository.ConfigurableListDetailsRepository;
import com.disputetrackingsystem.repository.DisputeRepository;
import com.disputetrackingsystem.repository.SavingsAccountTransactionRepository;
import com.disputetrackingsystem.model.User;
import com.disputetrackingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    //GET STATUS BY NAME & DETAILS
    private ConfigurableListDetails getStatusByName(String listName, String detailsName) {
        return configurableListDetailsRepository.findByListNameAndDetailsName(listName, detailsName);
    }
    private ConfigurableListDetails getsubStatusByName(String listName, String detailsName) {
        return configurableListDetailsRepository.findByListNameAndDetailsName(listName, detailsName);
    }

    //RAISE DISPUTE
    public Dispute createDispute(Dispute dispute) {
        // Get logged-in user from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Set createdBy automatically
        dispute.setCreatedBy(user);
        //Fetch TXN-Id
        Long savingsAccountTransactionId = dispute.getSavingsAccountTransaction().getId();
        // Link TXN-Id
        SavingsAccountTransaction savingsAccountTransaction = savingsAccountTransactionRepository.findById(savingsAccountTransactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        dispute.setSavingsAccountTransaction(savingsAccountTransaction);

        // Check if dispute already exists for this transaction
        boolean exists = disputeRepository.existsBySavingsAccountTransaction(savingsAccountTransaction);
        if (exists) {
            throw new IllegalArgumentException(
                    "A dispute already exists for transaction ID: " + savingsAccountTransactionId
            );
        }
        // Set initial status to INITIATED
        ConfigurableListDetails initiatedStatus = getStatusByName("status", "INITIATED");
        dispute.setStatus(initiatedStatus);
        // Set initial sub-status to PENDING REVIEW
        ConfigurableListDetails initiatedSubStatus = getsubStatusByName("sub_status", "PENDING_REVIEW");
        dispute.setSubStatus(initiatedSubStatus);

        return disputeRepository.save(dispute);
    }

    //SHOW DISPUTE BY ID
    public Dispute getDisputeById(Long id) {
        return disputeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispute not found"));
    }

    //SHOW FILTERED DISPUTES(ALL/UNDER-VERIFICATION/UNDER-REVIEWED)
    public Page<Dispute> showAllDispute(Pageable pageable, String filter) {
        PageRequest pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("createdDate").descending()
        );

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

    //SHOW DISPUTES BY ACCOUNT NUMBER
    public Page<Dispute> getDisputesByAccountNumber(long accountNumber, Pageable pageable) {
        return disputeRepository
                .findBySavingsAccountTransaction_SavingsAccount_AccountNumber(accountNumber, pageable);
    }

    //SHOW DISPUTE DASHBOARD
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> dashboardData = new HashMap<>();

        // STATUS WISE COUNT (query using DB uppercase, return nice labels)
        Map<String, Long> subStatusCounts = new HashMap<>();
        subStatusCounts.put("Pending Review", disputeRepository.countBySubStatus_NameIgnoreCase("PENDING_REVIEW"));
        subStatusCounts.put("Under Review", disputeRepository.countBySubStatus_NameIgnoreCase("UNDER_REVIEW"));
        subStatusCounts.put("Accepted", disputeRepository.countBySubStatus_NameIgnoreCase("ACCEPTED"));
        subStatusCounts.put("Rejected", disputeRepository.countBySubStatus_NameIgnoreCase("REJECTED"));
        subStatusCounts.put("Partially Accepted", disputeRepository.countBySubStatus_NameIgnoreCase("PARTIALLY_ACCEPTED"));

        // TIME BASED COUNT
        Map<String, Long> timeCounts = new HashMap<>();

        LocalDate today = LocalDate.now();
        LocalDateTime startOfToday = today.atStartOfDay();
        LocalDateTime now = LocalDateTime.now();

        // convert LocalDateTime → java.util.Date
        Date startOfTodayDate = Date.from(startOfToday.atZone(ZoneId.systemDefault()).toInstant());
        Date nowDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        // Today
        timeCounts.put("Today", disputeRepository.countByCreatedDateBetween(startOfTodayDate, nowDate));

        // This Week
        timeCounts.put("This Week", disputeRepository.countByCreatedDateAfter(
                Date.from(today.minusWeeks(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
        ));

        // This Month
        timeCounts.put("This Month", disputeRepository.countByCreatedDateAfter(
                Date.from(today.minusMonths(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
        ));

        // This Year
        timeCounts.put("This Year", disputeRepository.countByCreatedDateAfter(
                Date.from(today.minusYears(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
        ));

        // Total
        timeCounts.put("Total", disputeRepository.count());

        dashboardData.put("subStatusCounts", subStatusCounts);
        dashboardData.put("timeCounts", timeCounts);

        return dashboardData;
    }

    //DELETE DISPUTE BY ID
    public void deleteDispute(Long id) {
        if (!disputeRepository.existsById(id)) {
            throw new RuntimeException("Dispute not found");
        }
        disputeRepository.deleteById(id);
    }

    // UPDATE DISPUTE
    public Dispute updateDisputeStatusAndSubStatus(Long disputeId,
                                                   String newStatusName,
                                                   String newSubStatusName,
                                                   String comments,
                                                   BigDecimal refund,
                                                   Boolean vendorVerified) {
        Dispute dispute = getDisputeById(disputeId);

        ConfigurableListDetails newStatus = getStatusByName("status", newStatusName);
        ConfigurableListDetails newSubStatus = getStatusByName("sub_status", newSubStatusName);

        dispute.setStatus(newStatus);
        dispute.setSubStatus(newSubStatus);
        dispute.setComments(comments);
        dispute.setRefund(refund);

        if (vendorVerified != null) {
            dispute.setVendorVerified(vendorVerified);  // save checkbox value
        }

        // Set reviewedBy = logged-in manager
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User manager = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        dispute.setReviewedBy(manager);

        return disputeRepository.save(dispute);
    }

    //GET DISPUTE CREATE & REVIEW STATS FOR EACH USER
    public Map<String, Long> getDisputeStatsForCurrentUser(Long userId) {
        Object result = disputeRepository.getDisputeStatsForUser(userId);
        Object[] counts = result != null ? (Object[]) result : new Object[] {0L, 0L};

        Map<String, Long> stats = new HashMap<>();
        stats.put("disputesCreated", counts[0] != null ? ((Number) counts[0]).longValue() : 0L);
        stats.put("disputesReviewed", counts[1] != null ? ((Number) counts[1]).longValue() : 0L);

        return stats;
    }

    //GET RECENT DISPUTES
    public List<Dispute> getRecentDisputes() {
        return disputeRepository.findTop10ByOrderByCreatedDateDesc();
    }
}
