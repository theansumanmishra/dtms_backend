package com.disputetrackingsystem.controller;

import com.disputetrackingsystem.DTO.DisputeStatusUpdateRequest;
import com.disputetrackingsystem.model.Dispute;
import com.disputetrackingsystem.model.Reason;
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
import java.util.Map;

@RestController
@RequestMapping("/disputes")
public class DisputeController {

    @Autowired
    private DisputeService disputeService;

    @Autowired
    private ReasonService reasonService;

    // RAISE DISPUTE
    @PreAuthorize("hasAuthority('CREATE_DISPUTE')")
    @PostMapping
    public Dispute createDispute(@RequestBody Dispute dispute) {
        return disputeService.createDispute(dispute);
    }

    // VIEW DISPUTE BY ID
    @PreAuthorize("hasAuthority('VIEW_DISPUTE')")
    @GetMapping("/{id}")
    public Dispute getEachDispute(@PathVariable long id) {
        return disputeService.getDisputeById(id);
    }

    // GET ALL DISPUTES WITH SORTED BY disputeCreatedDate & PAGINATION &
    // PENDING-REVIEW
    @PreAuthorize("hasAuthority('VIEW_DISPUTE')")
    @GetMapping
    public ResponseEntity<Page<Dispute>> getAllDisputes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "all") String filter) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Dispute> disputes = disputeService.showAllDispute(pageable, filter);
        return ResponseEntity.ok(disputes);
    }

    // GET DISPUTES BY ACCOUNT NUMBER
    @PreAuthorize("hasAuthority('VIEW_DISPUTE')")
    @GetMapping("/search")
    public ResponseEntity<Page<Dispute>> getDisputesByAccountNumber(
            @RequestParam long accountNumber,
            Pageable pageable) {
        Page<Dispute> disputes = disputeService.getDisputesByAccountNumber(accountNumber, pageable);
        return ResponseEntity.ok(disputes);
    }

    // GET DISPUTE DASHBOARD DATA
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        return ResponseEntity.ok(disputeService.getDashboardStats());
    }

    // UPDATE BOTH STATUS AND SUB-STATUS
    @PreAuthorize("hasAuthority('REVIEW_DISPUTE')")
    @PutMapping("/{disputeId}")
    public ResponseEntity<Dispute> updateStatusAndSubStatus(
            @PathVariable Long disputeId,
            @RequestBody DisputeStatusUpdateRequest request) {
        Dispute updatedDispute = disputeService.updateDisputeStatusAndSubStatus(
                disputeId,
                request.getStatusName(),
                request.getSubStatusName(),
                request.getComments(),
                request.getRefund(),
                request.getVendorVerified());
        return ResponseEntity.ok(updatedDispute);
    }

    // DELETE DISPUTE BY ID
    @PreAuthorize("hasAuthority('MANAGE_DISPUTE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDispute(@PathVariable Long id) {
        disputeService.deleteDispute(id);
        return ResponseEntity.ok("Dispute with ID " + id + " has been deleted.");
    }

    // GET ALL REASONS
    @GetMapping("/reasons")
    public List<Reason> getAllReasons() {
        return reasonService.getAllReasons();
    }

    // GET RECENT DISPUTES(TOP 10)
    @GetMapping("/recent")
    public ResponseEntity<List<Dispute>> getRecentDisputes() {
        List<Dispute> recentDisputes = disputeService.getRecentDisputes();
        return ResponseEntity.ok(recentDisputes);
    }
}
