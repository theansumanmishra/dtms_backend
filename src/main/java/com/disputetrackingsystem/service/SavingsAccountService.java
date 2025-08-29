package com.disputetrackingsystem.service;

import com.disputetrackingsystem.entity.Client;
import com.disputetrackingsystem.entity.SavingsAccount;
import com.disputetrackingsystem.repository.ClientRepository;
import com.disputetrackingsystem.repository.SavingsAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavingsAccountService {

    @Autowired
    public SavingsAccountRepository savingsAccountRepository;

    @Autowired
    public ClientRepository clientRepository;

    //SAVINGS ACCOUNT
    public SavingsAccount createSavingsAccount(SavingsAccount savingsAccount){
        Long clientId = savingsAccount.getClient().getId();
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        savingsAccount.setClient(client);
        return savingsAccountRepository.save(savingsAccount);
    }

    //SHOW ALL SAVINGS ACCOUNT
    public List<SavingsAccount> showAllSavingsAccount(){
        return savingsAccountRepository.findAll();
    }

    //SAVINGS ACCOUNT BY ID
    public SavingsAccount getAccountById(long id){
        return savingsAccountRepository.findById(id).orElseThrow(()->new RuntimeException("Savings Account doesn't exist"));
    }
}
