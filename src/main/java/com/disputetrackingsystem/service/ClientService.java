package com.disputetrackingsystem.service;

import com.disputetrackingsystem.entity.Client;
import com.disputetrackingsystem.repository.ClientRepository;
import com.disputetrackingsystem.repository.DebitCardRepository;
import com.disputetrackingsystem.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DebitCardRepository debitCardRepository;

    //CREATE CLIENT
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    //CREATE MULTIPLE CLIENTS
//    public List<Client> saveClients(List<Client> clients) {
//        return clientRepository.saveAll(clients);
//    }

    //SHOW CLIENTS BY ID
    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }

    //SHOW ALL CLIENTS
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    //UPDATE CLIENTS
    public Client updateClient(Client client) {
        return clientRepository.save(client);
    }

    //DELETE CLIENT
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Client not found");
        }
        clientRepository.deleteById(id);
    }

    //FIND USER BY USERNAME
    public Client findByClientName(String name){
        return clientRepository.findClientByName(name)
                .orElseThrow(()-> new RuntimeException("Client not Found"));
    }

    //FIND USER BY PHONE NUMBER
    public Client findClientByPhone(String phone){
        return clientRepository.findClientByPhone(phone)
                .orElseThrow(()-> new RuntimeException("Client not Found"));
    }
}
