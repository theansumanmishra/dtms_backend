package com.disputetrackingsystem.service;

import com.disputetrackingsystem.entity.DebitCard;
import com.disputetrackingsystem.entity.SavingsAccount;
import com.disputetrackingsystem.repository.DebitCardRepository;
import com.disputetrackingsystem.repository.SavingsAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebitCardService {

    @Autowired
    private DebitCardRepository debitCardRepository;

    @Autowired
    SavingsAccountRepository savingsAccountRepository;

    //CREATE DEBIT CARD BY SAVINGS ACCOUNT ID
    public DebitCard createDebitCard(DebitCard debitCard){
        //FETCH SAVINGS ACCOUNT ID
        Long savingsAccountId = debitCard.getSavingsAccount().getId();
        SavingsAccount savingsAccount = savingsAccountRepository.findById(savingsAccountId)
                .orElseThrow(() -> new RuntimeException("Savings Account not found"));
        debitCard.setSavingsAccount(savingsAccount);
        return debitCardRepository.save(debitCard);
    }

    //SHOW ALL DEBIT CARDS BY SAVINGS ACCOUNT ID
    public List<DebitCard> showAllDebitCards(){
        return debitCardRepository.findAll();
    }

    //GET DEBIT CARD BY ID
    public DebitCard getDebitById(Long id){
        return debitCardRepository.findById(id).orElseThrow(()-> new RuntimeException("Debit card not found"));
    }

    //GET DEBIT CARD BY SAVINGS ACCOUNT ID
    public List<DebitCard> findBySavingsAccountId(Long savingsAccountId){
        return debitCardRepository.findBySavingsAccountId(savingsAccountId);
    }
}
