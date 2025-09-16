package com.disputetrackingsystem.controller;

import com.disputetrackingsystem.entity.SavingsAccountTransaction;
import com.disputetrackingsystem.repository.SavingsAccountTransactionRepository;
import com.disputetrackingsystem.service.DebitCardService;
import com.disputetrackingsystem.service.SavingsAccountTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class SavingsAccountTransactionController {

    @Autowired
    private SavingsAccountTransactionRepository savingsAccountTransactionRepository;

    @Autowired
    private SavingsAccountTransactionService savingsAccountTransactionService;

    //CREATE SAVINGS ACCOUNT TRANSACTION ##
    @PostMapping
    public SavingsAccountTransaction postSavingsAccountTransaction(@RequestBody SavingsAccountTransaction savingsAccountTransaction){
        return savingsAccountTransactionService.createSavingsAccountTransaction(savingsAccountTransaction);
    }

    //GET ALL SAVINGS ACCOUNT TRANSACTIONS 90 DAYS##
    @GetMapping("/{savingsAccountId}")
    public List<SavingsAccountTransaction> getTransactions(
            @PathVariable Long savingsAccountId,
            @RequestParam(value = "fetchOnlyLast90", required = false, defaultValue = "false") boolean fetchOnlyLast90) {

        return savingsAccountTransactionService.getLast90DaysTransactions(savingsAccountId);
    }

    @GetMapping("/{id}/similar")
    public List<SavingsAccountTransaction> getTransactionsBySameSubType(@PathVariable Long id) {
        return savingsAccountTransactionService.getTransactionsBySameSubType(id);
    }
}

