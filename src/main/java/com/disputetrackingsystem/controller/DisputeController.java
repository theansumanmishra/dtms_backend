package com.disputetrackingsystem.controller;

import com.disputetrackingsystem.DTO.DisputeStatusUpdateRequest;
import com.disputetrackingsystem.entity.Dispute;
import com.disputetrackingsystem.entity.Reason;
import com.disputetrackingsystem.repository.DisputeRepository;
import com.disputetrackingsystem.service.DisputeService;
import com.disputetrackingsystem.service.ReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasRole;

@RestController
@RequestMapping("/disputes")
public class DisputeController {

    @Autowired
    private DisputeService disputeService;

    @Autowired
    private DisputeRepository disputeRepository;

    @Autowired
    private ReasonService reasonService;

    //RAISE DISPUTE
    @PreAuthorize("hasAuthority('CREATE_DISPUTE')")
    @PostMapping
    public Dispute createDispute(@RequestBody Dispute dispute) {
        return disputeService.createDispute(dispute);
    }

    //VIEW DISPUTE BY ID
    @PreAuthorize("hasAuthority('VIEW_DISPUTE')")
    @GetMapping("/{id}")
    public Dispute getEachDispute(@PathVariable long id) {
        return disputeService.getDisputeById(id);
    }

    //GET ALL DISPUTES WITH SORTED BY disputeCreatedDate & PAGINATION
    @PreAuthorize("hasAuthority('VIEW_DISPUTE')")
    @GetMapping
    public ResponseEntity<Page<Dispute>> getAllDisputes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Dispute> disputes = disputeService.showAllDispute(pageable);
        return ResponseEntity.ok(disputes);
    }

//    //UPDATE ONLY STATUS
//    @PreAuthorize("hasAuthority('REVIEW_DISPUTE')")
//    @PutMapping("/{disputeId}/status")
//    public ResponseEntity<Dispute> updateOnlyStatus(
//            @PathVariable Long disputeId,
//            @RequestParam String statusName) {
//        Dispute updatedDispute = disputeService.updateDisputeStatus(disputeId, statusName);
//        return ResponseEntity.ok(updatedDispute);
//    }
//
//    //UPDATE ONLY SUB-STATUS
//    @PreAuthorize("hasAuthority('REVIEW_DISPUTE')")
//    @PutMapping("/{disputeId}/substatus")
//    public Dispute updateOnlySubStatus(
//            @PathVariable Long disputeId,
//            @RequestParam String subStatusName) {
//        return disputeService.updateDisputeSubStatus(disputeId, subStatusName);
//    }

    // UPDATE BOTH STATUS AND SUB-STATUS
    @PreAuthorize("hasAuthority('REVIEW_DISPUTE')")
    @PutMapping("/{disputeId}")
    public ResponseEntity<Dispute> updateStatusAndSubStatus(
            @PathVariable Long disputeId,
            @RequestBody DisputeStatusUpdateRequest request) {
        Dispute updatedDispute = disputeService.updateDisputeStatusAndSubStatus(
                disputeId,
                request.getStatusName(),
                request.getSubStatusName()
        );
        return ResponseEntity.ok(updatedDispute);
    }

    // DELETE DISPUTE BY ID
    @PreAuthorize("hasAuthority('MANAGE_DISPUTE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDispute(@PathVariable Long id) {
        disputeService.deleteDispute(id);
        return ResponseEntity.ok("Dispute with ID " + id + " has been deleted.");
    }

    //GET ALL REASONS
    @GetMapping("/reasons")
    public List<Reason> getAllReasons() {
        return reasonService.getAllReasons();
    }
}

