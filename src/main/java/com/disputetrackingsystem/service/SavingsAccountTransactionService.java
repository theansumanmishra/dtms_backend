package com.disputetrackingsystem.service;

import com.disputetrackingsystem.entity.Client;
import com.disputetrackingsystem.entity.DebitCard;
import com.disputetrackingsystem.entity.SavingsAccount;
import com.disputetrackingsystem.entity.SavingsAccountTransaction;
import com.disputetrackingsystem.repository.DebitCardRepository;
import com.disputetrackingsystem.repository.SavingsAccountRepository;
import com.disputetrackingsystem.repository.SavingsAccountTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavingsAccountTransactionService {

    @Autowired
    private SavingsAccountTransactionRepository savingsAccountTransactionRepository;

    @Autowired
    private SavingsAccountRepository savingsAccountRepository;

    @Autowired
    private DebitCardRepository debitCardRepository;

    //CREATE A SAVINGS ACCOUNT TRANSACTION
    public SavingsAccountTransaction createSavingsAccountTransaction(SavingsAccountTransaction savingsAccountTransaction){

        //FETCH SAVINGS ACCOUNT ID
        Long savingsAccountId = savingsAccountTransaction.getSavingsAccount().getId();
        SavingsAccount savingsAccount = savingsAccountRepository.findById(savingsAccountId)
                .orElseThrow(() -> new RuntimeException("Savings Account not found"));
        savingsAccountTransaction.setSavingsAccount(savingsAccount);

        //FETCH DEBIT CARD ID
        Long debitCardId = savingsAccountTransaction.getDebitCard().getId();
        DebitCard debitCard = debitCardRepository.findById(debitCardId)
                .orElseThrow(() -> new RuntimeException("Debit card not found"));
        savingsAccountTransaction.setDebitCard(debitCard);

        return savingsAccountTransactionRepository.save(savingsAccountTransaction);
    }

    //VIEW ALL SAVINGS ACCOUNT TRANSACTION BY SAVINGS ACCOUNT ID
    public List<SavingsAccountTransaction> findBySavingsAccountId(Long savingsAccountId) {
        return savingsAccountTransactionRepository.findBySavingsAccountId(savingsAccountId);
    }

    //SAVINGS ACCOUNT TRANSACTION BY ID
    public SavingsAccountTransaction getTransactionById(Long id){
        return savingsAccountTransactionRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Transaction not found"));
    }

    //GET ALL SAVINGS ACCOUNT TRANSACTIONS
    public List<SavingsAccountTransaction> getAllTransactions(){
        return savingsAccountTransactionRepository.findAll();
    }
}
