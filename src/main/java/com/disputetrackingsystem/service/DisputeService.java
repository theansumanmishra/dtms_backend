package com.disputetrackingsystem.service;

import com.disputetrackingsystem.model.ConfigurableListDetails;
import com.disputetrackingsystem.model.Dispute;
import com.disputetrackingsystem.model.SavingsAccountTransaction;
import com.disputetrackingsystem.repository.ConfigurableListDetailsRepository;
import com.disputetrackingsystem.repository.DisputeRepository;
import com.disputetrackingsystem.repository.SavingsAccountTransactionRepository;
import com.disputetrackingsystem.security.model.User;
import com.disputetrackingsystem.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
        // Set initial status to INITIATED
        ConfigurableListDetails initiatedStatus = getStatusByName("status", "INITIATED");
        dispute.setStatus(initiatedStatus);
        // Set initial sub-status to UNDER REVIEW
        ConfigurableListDetails initiatedSubStatus = getsubStatusByName("sub_status", "UNDER REVIEW");
        dispute.setSubStatus(initiatedSubStatus);

        return disputeRepository.save(dispute);
    }

    //SHOW DISPUTE BY ID
    public Dispute getDisputeById(Long id) {
        return disputeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispute not found"));
    }

    //SHOW ALL DISPUTES
    public Page<Dispute> showAllDispute(Pageable pageable) {
        return disputeRepository.findAll(pageable);
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
                                                   String comments) {
        Dispute dispute = getDisputeById(disputeId);

        ConfigurableListDetails newStatus = getStatusByName("status", newStatusName);
        ConfigurableListDetails newSubStatus = getStatusByName("sub_status", newSubStatusName);

        dispute.setStatus(newStatus);
        dispute.setSubStatus(newSubStatus);
        dispute.setComments(comments);

        // Set reviewedBy = logged-in manager
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User manager = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        dispute.setReviewedBy(manager);

        return disputeRepository.save(dispute);
    }
}
