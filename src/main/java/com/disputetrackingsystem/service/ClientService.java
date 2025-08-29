package com.disputetrackingsystem.service;

import com.disputetrackingsystem.entity.Client;
import com.disputetrackingsystem.repository.ClientRepository;
import com.disputetrackingsystem.repository.DebitCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DebitCardRepository debitCardRepository;

    //CREATE CLIENTS
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    //SHOW CLIENTS BY ID
    public Client getClientById(Long id){
        return clientRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Client not found"));
    }

    //SHOW ALL CLIENTS
    public List<Client> getAllClients(){
        return clientRepository.findAll();
    }

    //UPDATE CLIENTS
    public Client updateClient(Client client) {
        return clientRepository.save(client);
    }
}
