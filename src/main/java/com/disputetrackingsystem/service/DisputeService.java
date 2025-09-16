package com.disputetrackingsystem.service;

import com.disputetrackingsystem.entity.ConfigurableListDetails;
import com.disputetrackingsystem.entity.Dispute;
import com.disputetrackingsystem.entity.SavingsAccountTransaction;
import com.disputetrackingsystem.repository.ConfigurableListDetailsRepository;
import com.disputetrackingsystem.repository.DisputeRepository;
import com.disputetrackingsystem.repository.SavingsAccountTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class DisputeService {

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

    //CREATE A SINGLE DISPUTE
    public Dispute createDispute(Dispute dispute) {
//        //Fetch ClientId
//        Long clientId = dispute.getClient().getId();
//
//        // Link ClientId
//        Client client = clientRepository.findById(clientId)
//                .orElseThrow(() -> new RuntimeException("Client not found"));
//        dispute.setClient(client);

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

//    // UPDATE ONLY STATUS
//    public Dispute updateDisputeStatus(Long disputeId, String newStatusName) {
//        Dispute dispute = getDisputeById(disputeId);
//        ConfigurableListDetails newStatus = getStatusByName("status", newStatusName);
//        dispute.setStatus(newStatus);
//        return disputeRepository.save(dispute);
//    }
//
//    // UPDATE ONLY SUB-STATUS
//    public Dispute updateDisputeSubStatus(Long disputeId, String newSubStatusName) {
//        Dispute dispute = getDisputeById(disputeId);
//        ConfigurableListDetails newSubStatus = getStatusByName("sub_status", newSubStatusName);
//        dispute.setSubStatus(newSubStatus);
//        return disputeRepository.save(dispute);
//    }

    // UPDATE BOTH STATUS AND SUB-STATUS
    public Dispute updateDisputeStatusAndSubStatus(Long disputeId, String newStatusName, String newSubStatusName) {
        Dispute dispute = getDisputeById(disputeId);

        ConfigurableListDetails newStatus = getStatusByName("status", newStatusName);
        ConfigurableListDetails newSubStatus = getStatusByName("sub_status", newSubStatusName);

        dispute.setStatus(newStatus);
        dispute.setSubStatus(newSubStatus);

        return disputeRepository.save(dispute);
    }
}
