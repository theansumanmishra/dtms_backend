package com.disputetrackingsystem.controller;

import com.disputetrackingsystem.entity.DebitCard;
import com.disputetrackingsystem.entity.SavingsAccount;
import com.disputetrackingsystem.entity.SavingsAccountTransaction;
import com.disputetrackingsystem.repository.SavingsAccountRepository;
import com.disputetrackingsystem.service.DebitCardService;
import com.disputetrackingsystem.service.SavingsAccountService;
import com.disputetrackingsystem.service.SavingsAccountTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('MANAGER')")
@RestController
@RequestMapping("/savingsaccounts")
public class SavingsAccountController {

    @Autowired
    private SavingsAccountService savingsAccountService;

    @Autowired
    private SavingsAccountRepository savingsAccountRepository;

    @Autowired
    private DebitCardService debitCardService;

    @Autowired
    private SavingsAccountTransactionService savingsAccountTransactionService;

    //CREATE SAVINGS ACCOUNT ##
    @PostMapping
    public SavingsAccount postSavingsAccount(@RequestBody SavingsAccount savingsAccount) {
        return savingsAccountService.createSavingsAccount(savingsAccount);
    }

    //SHOW ALL SAVINGS ACCOUNT ##
    @GetMapping
    public List<SavingsAccount> getAllSavingsAccount() {
        return savingsAccountService.showAllSavingsAccount();
    }

    //SHOW SAVINGS ACCOUNT BY ID ##
    @GetMapping("/{id}")
    public SavingsAccount getAccountById(@PathVariable Long id){
        return savingsAccountService.getAccountById(id);
    }

    // ************* SAVINGS_ACCOUNT TRANSACTIONS ******************//

    //VIEW TRANSACTION BY SAVINGS-ACCOUNT ID
    @GetMapping("/{savingsAccountId}/transactions")
    public List<SavingsAccountTransaction> getAllSavingsAccountTransaction(@PathVariable Long savingsAccountId){
        return savingsAccountTransactionService.findBySavingsAccountId(savingsAccountId);
    }

//    //SHOW EACH TRANSACTION BY ID
//    @GetMapping("/{savingsaccountId}/transactions/{id}")
//    public SavingsAccountTransaction getTransactionById(@PathVariable Long id){
//        return savingsAccountTransactionService.getTransactionById(id);
//    }

    //VIEW DEBIT-CARDS BY SAVINGS-ACCOUNT ID
    @GetMapping("{savingsAccountId}/debitcards")
    public List<DebitCard> getAllDebitCards(@PathVariable Long savingsAccountId){
        return debitCardService.findBySavingsAccountId(savingsAccountId);
    }
}
