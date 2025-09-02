package com.disputetrackingsystem.service;

import com.disputetrackingsystem.entity.Client;
import com.disputetrackingsystem.entity.ConfigurableListDetails;
import com.disputetrackingsystem.entity.Dispute;
import com.disputetrackingsystem.repository.ClientRepository;
import com.disputetrackingsystem.repository.ConfigurableListDetailsRepository;
import com.disputetrackingsystem.repository.DisputeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DisputeService {

    @Autowired
    private DisputeRepository disputeRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ConfigurableListDetailsRepository configurableListDetailsRepository;

    //GET STATUS BY NAME & DETAILS
    private ConfigurableListDetails getStatusByName(String listName, String detailsName) {
        return configurableListDetailsRepository.findByListNameAndDetailsName(listName, detailsName);
    }

    private ConfigurableListDetails getsubStatusByName(String listName, String detailsName) {
        return configurableListDetailsRepository.findByListNameAndDetailsName(listName, detailsName);
    }

    //CREATE DISPUTES
    public List<Dispute> createDispute(List<Dispute> disputes) {

        List<Dispute> savedDisputes = new ArrayList<>();
        for (Dispute dispute : disputes) {
            //Fetch ClientId
            Long clientId = dispute.getClient().getId();

            // Link ClientId
            Client client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new RuntimeException("Client not found"));
            dispute.setClient(client);

            // Set initial status to INITIATED
            ConfigurableListDetails initiatedStatus = getStatusByName("status", "INITIATED");
            dispute.setStatus(initiatedStatus);

            // Set initial sub-status to UNDER REVIEW
            ConfigurableListDetails initiatedSubStatus = getsubStatusByName("sub_status", "UNDER REVIEW");
            dispute.setSubStatus(initiatedSubStatus);

            savedDisputes.add(disputeRepository.save(dispute));
        }
        return savedDisputes;

        // Single Dispute Creation
        //Fetch ClientId
//        Long clientId = dispute.getClient().getId();
//
        // Link ClientId
//        Client client = clientRepository.findById(clientId)
//                .orElseThrow(() -> new RuntimeException("Client not found"));
//        dispute.setClient(client);
//
        // Set initial status to INITIATED
//        ConfigurableListDetails initiatedStatus = getStatusByName("status", "INITIATED");
//        dispute.setStatus(initiatedStatus);
//
        // Set initial sub-status to UNDER REVIEW
//        ConfigurableListDetails initiatedSubStatus = getsubStatusByName("sub_status", "UNDER REVIEW");
//        dispute.setSubStatus(initiatedSubStatus);
//
//        return disputeRepository.save(dispute);
    }

    // UPDATE STATUS ONLY
    public Dispute updateDisputeStatus(Long disputeId, String newStatusName) {
        Dispute dispute = getDisputeById(disputeId);
        ConfigurableListDetails newStatus = getStatusByName("status", newStatusName);
        dispute.setStatus(newStatus);
        return disputeRepository.save(dispute);
    }

    // UPDATE SUB-STATUS ONLY
    public Dispute updateDisputeSubStatus(Long disputeId, String newSubStatusName) {
        Dispute dispute = getDisputeById(disputeId);
        ConfigurableListDetails newSubStatus = getStatusByName("sub_status", newSubStatusName);
        dispute.setSubStatus(newSubStatus);
        return disputeRepository.save(dispute);
    }

    // UPDATE BOTH STATUS AND SUB-STATUS
    public Dispute updateDisputeStatusAndSubStatus(Long disputeId, String newStatusName, String newSubStatusName) {
        Dispute dispute = getDisputeById(disputeId);

        ConfigurableListDetails newStatus = getStatusByName("status", newStatusName);
        ConfigurableListDetails newSubStatus = getStatusByName("sub_status", newSubStatusName);

        dispute.setStatus(newStatus);
        dispute.setSubStatus(newSubStatus);

        return disputeRepository.save(dispute);
    }

    //SHOW DISPUTE BY ID
    public Dispute getDisputeById(Long id) {
        return disputeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispute not found"));
    }

    //SHOW ALL DISPUTES
    public Page<Dispute> showAllDispute(Pageable pageable) {
        return disputeRepository.findAll(
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("disputeCreatedDate").descending())
        );
    }

    //DELETE DISPUTE BY ID
    public void deleteDispute(Long id) {
        if (!disputeRepository.existsById(id)) {
            throw new RuntimeException("Dispute not found");
        }
        disputeRepository.deleteById(id);
    }
}
