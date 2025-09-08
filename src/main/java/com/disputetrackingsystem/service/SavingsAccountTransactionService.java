package com.disputetrackingsystem.service;

import com.disputetrackingsystem.entity.DebitCard;
import com.disputetrackingsystem.entity.SavingsAccount;
import com.disputetrackingsystem.entity.SavingsAccountTransaction;
import com.disputetrackingsystem.repository.DebitCardRepository;
import com.disputetrackingsystem.repository.SavingsAccountRepository;
import com.disputetrackingsystem.repository.SavingsAccountTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

//    //TRANSACTIONS BY TNX ID
//    public SavingsAccountTransaction getTransactionById(Long id){
//        return savingsAccountTransactionRepository.findById(id)
//                .orElseThrow(()->new RuntimeException("Transaction not found"));
//    }
//
//    //GET ALL SAVINGS ACCOUNT TRANSACTIONS
//    public List<SavingsAccountTransaction> getAllTransactions(){
//        return savingsAccountTransactionRepository.findAll();
//    }

    //GET LAST 90 DAYS TRANSACTIONS BY SAVINGS ACCOUNT ID
    public List<SavingsAccountTransaction> getLast90DaysTransactions(Long savingsAccountId) {
        return savingsAccountTransactionRepository.findAllFromDate(
                savingsAccountId, LocalDateTime.now().minusDays(90));
    }

    //GET TRANSACTIONS BY SAME TXN MODE (ATM or POS)
    public List<SavingsAccountTransaction> getTransactionsBySameSubType(Long id) {
        // Step 1: Find the clicked transaction
        SavingsAccountTransaction clickedTransaction = savingsAccountTransactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Step 2: Extract its type (ATM or POS)
        String mode = clickedTransaction.getTransactionMode();

        // Step 3: Fetch all with same mode
        return savingsAccountTransactionRepository.findByTransactionMode(mode);
    }
}
