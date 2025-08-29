package com.disputetrackingsystem.controller;

import com.disputetrackingsystem.entity.DisputeTransaction;
import com.disputetrackingsystem.repository.DisputeTransactionRepository;
import com.disputetrackingsystem.service.DisputeTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disputeTransaction")
public class DisputeTransactionController {

    @Autowired
    private DisputeTransactionService disputeTransactionService;

    @Autowired
    private DisputeTransactionRepository disputeTransactionRepository;

    //CREATE
    @PostMapping
    public DisputeTransaction createDisputeTransaction(@RequestBody DisputeTransaction disputeTransaction){
        return disputeTransactionService.createDisputeTransaction(disputeTransaction);
    }

    //SHOW BY ID
    @GetMapping("/{id}")
    public DisputeTransaction getDisputeTransactionById(@PathVariable Long id){
        return disputeTransactionService.disputeTransactionById(id);
    }

    //SHOW ALL
    @GetMapping
    public List<DisputeTransaction> allDisputeTransaction(){
        return disputeTransactionService.allDisputeTransaction();
    }
}
