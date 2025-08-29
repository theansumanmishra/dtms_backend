package com.disputetrackingsystem.service;

import com.disputetrackingsystem.entity.ConfigurableListDetails;
import com.disputetrackingsystem.entity.Dispute;
import com.disputetrackingsystem.entity.DisputeTransaction;
import com.disputetrackingsystem.entity.SavingsAccountTransaction;
import com.disputetrackingsystem.repository.ConfigurableListDetailsRepository;
import com.disputetrackingsystem.repository.DisputeRepository;
import com.disputetrackingsystem.repository.DisputeTransactionRepository;
import com.disputetrackingsystem.repository.SavingsAccountTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisputeTransactionService {

    @Autowired
    private DisputeTransactionRepository disputeTransactionRepository;

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
    //GET SUB-STATUS BY NAME & DETAILS
    private ConfigurableListDetails getsubStatusByName(String listName, String detailsName) {
        return configurableListDetailsRepository.findByListNameAndDetailsName(listName, detailsName);
    }

    //CREATE DISPUTE TRANSACTION
    public DisputeTransaction createDisputeTransaction(DisputeTransaction disputeTransaction){
        //FETCH & SET DISPUTE ID
        Long disputeId = disputeTransaction.getDispute().getId();
        Dispute dispute = disputeRepository.findById(disputeId)
                .orElseThrow(() -> new RuntimeException("Dispute not found"));
        disputeTransaction.setDispute(dispute);

        //FETCH & SET SAVINGSACCOUNT TRANSACTION ID
        Long savingsAccountTransactionId = disputeTransaction.getSavingsAccountTransaction().getId();
        SavingsAccountTransaction savingsAccountTransaction = savingsAccountTransactionRepository.findById(savingsAccountTransactionId)
                .orElseThrow(() -> new RuntimeException("Savings Account Transaction not found"));
        disputeTransaction.setSavingsAccountTransaction(savingsAccountTransaction);

        // Set initial status to INITIATED
        ConfigurableListDetails initiatedStatus = getStatusByName("status", "INITIATED");
        disputeTransaction.setStatus(initiatedStatus);

        // Set initial sub-status to PENDING
        ConfigurableListDetails initiatedSubStatus = getsubStatusByName("substatus", "PENDING");
        disputeTransaction.setSubStatus(initiatedSubStatus);

        return disputeTransactionRepository.save(disputeTransaction);
    }

    //SHOW BY ID
    public DisputeTransaction disputeTransactionById(Long id){
        return disputeTransactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Savings Account not found"));
    }

    //SHOW ALL
    public List<DisputeTransaction> allDisputeTransaction(){
        return disputeTransactionRepository.findAll();
    }
}
