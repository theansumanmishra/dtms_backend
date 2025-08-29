package com.disputetrackingsystem.controller;

import com.disputetrackingsystem.entity.SavingsAccountTransaction;
import com.disputetrackingsystem.repository.SavingsAccountTransactionRepository;
import com.disputetrackingsystem.service.DebitCardService;
import com.disputetrackingsystem.service.SavingsAccountTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/savingsaccounttransactions")
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
//    @GetMapping
//    public List<SavingsAccountTransaction> getAllTransactions(@RequestParam("fetchOnlyLast90") boolean fetchOnlyLast90){
//        return savingsAccountTransactionService.getAllTransactions();
//    }
}

