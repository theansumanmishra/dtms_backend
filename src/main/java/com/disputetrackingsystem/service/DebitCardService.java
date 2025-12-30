package com.disputetrackingsystem.service;

import com.disputetrackingsystem.model.DebitCard;
import com.disputetrackingsystem.model.SavingsAccount;
import com.disputetrackingsystem.repository.DebitCardRepository;
import com.disputetrackingsystem.repository.SavingsAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.lang.NonNull;

@Service
public class DebitCardService {

    @Autowired
    private DebitCardRepository debitCardRepository;

    @Autowired
    SavingsAccountRepository savingsAccountRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    // CREATE DEBIT CARD BY SAVINGS ACCOUNT ID
    public DebitCard createDebitCard(DebitCard debitCard) {
        // FETCH SAVINGS ACCOUNT ID
        Long savingsAccountId = debitCard.getSavingsAccount().getId();
        if (savingsAccountId == null) {
            throw new IllegalArgumentException("Savings Account ID cannot be null");
        }
        SavingsAccount savingsAccount = savingsAccountRepository.findById(savingsAccountId)
                .orElseThrow(() -> new RuntimeException("Savings Account not found"));
        debitCard.setSavingsAccount(savingsAccount);

        // ENCODE DEBIT-CARD PIN
        String cryptPin = String.valueOf(debitCard.getPin());
        debitCard.setPin(encoder.encode(cryptPin));

        return debitCardRepository.save(debitCard);
    }

    // SHOW ALL DEBIT CARDS BY SAVINGS ACCOUNT ID
    public List<DebitCard> showAllDebitCards() {
        return debitCardRepository.findAll();
    }

    // GET DEBIT CARD BY ID
    public DebitCard getDebitById(@NonNull Long id) {
        return debitCardRepository.findById(id).orElseThrow(() -> new RuntimeException("Debit card not found"));
    }

    // GET DEBIT CARD BY SAVINGS ACCOUNT ID
    public List<DebitCard> findBySavingsAccountId(Long savingsAccountId) {
        return debitCardRepository.findBySavingsAccountId(savingsAccountId);
    }
}
