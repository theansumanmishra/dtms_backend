package com.disputetrackingsystem.controller;

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

@RestController
@RequestMapping("/disputes")
public class DisputeController {

    @Autowired
    private DisputeService disputeService;

    @Autowired
    private DisputeRepository disputeRepository;

    @Autowired
    private ReasonService reasonService;

    //CREATE DISPUTE    ##
    @PostMapping("/create")
    public Dispute createDispute(@RequestBody Dispute dispute) {
        return disputeService.createDispute(dispute);
    }

    //VIEW DISPUTE BY ID    ##
    @GetMapping("/{id}")
    public Dispute getEachDispute(@PathVariable long id) {
        return disputeService.getDisputeById(id);
    }

    //GET ALL DISPUTES WITH PAGINATION ##
    @PreAuthorize("hasAuthority('DISPUTE_VIEW')")
    @GetMapping
    public ResponseEntity<Page<Dispute>> getAllDisputes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size)
    {
        Pageable pageable = PageRequest.of(page, size, Sort.by("disputeCreatedDate").descending());
        Page<Dispute> disputes = disputeService.showAllDispute(pageable);
        return ResponseEntity.ok(disputes);
    }

    //GET ALL REASONS
    @GetMapping("/reasons")
    public List<Reason> getAllReasons() {
        return reasonService.getAllReasons();
    }

    //UPDATE ONLY STATUS
    @PutMapping("/{disputeId}/status")
    public ResponseEntity<Dispute> updateOnlyStatus(
            @PathVariable Long disputeId,
            @RequestParam String statusName) {
        Dispute updatedDispute = disputeService.updateDisputeStatus(disputeId, statusName);
        return ResponseEntity.ok(updatedDispute);
    }

    //UPDATE ONLY SUB-STATUS
    @PutMapping("/{disputeId}/substatus")
    public Dispute updateOnlySubStatus(
            @PathVariable Long disputeId,
            @RequestParam String subStatusName) {
        return disputeService.updateDisputeSubStatus(disputeId, subStatusName);
    }

    // UPDATE BOTH STATUS AND SUB-STATUS
    @PutMapping("/{disputeId}/status-and-substatus")
    public ResponseEntity<Dispute> updateStatusAndSubStatus(
            @PathVariable Long disputeId,
            @RequestParam String statusName,
            @RequestParam String subStatusName) {
        Dispute updatedDispute = disputeService.updateDisputeStatusAndSubStatus(disputeId, statusName, subStatusName);
        return ResponseEntity.ok(updatedDispute);
    }
}
