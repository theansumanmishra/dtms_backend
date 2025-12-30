package com.disputetrackingsystem.controller;

import com.disputetrackingsystem.model.SavingsAccountTransaction;
import com.disputetrackingsystem.service.SavingsAccountTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class SavingsAccountTransactionController {

    @Autowired
    private SavingsAccountTransactionService savingsAccountTransactionService;

    // CREATE SAVINGS ACCOUNT TRANSACTION ##
    @PostMapping
    public SavingsAccountTransaction postSavingsAccountTransaction(
            @RequestBody SavingsAccountTransaction savingsAccountTransaction) {
        return savingsAccountTransactionService.createSavingsAccountTransaction(savingsAccountTransaction);
    }

    @GetMapping("/{id}/similar")
    public List<SavingsAccountTransaction> getTransactionsBySameSubType(@PathVariable Long id) {
        return savingsAccountTransactionService.getTransactionsBySameSubType(id);
    }
}
