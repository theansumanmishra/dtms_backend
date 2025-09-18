package com.disputetrackingsystem.service;

import com.disputetrackingsystem.model.Client;
import com.disputetrackingsystem.repository.ClientRepository;
import com.disputetrackingsystem.repository.DebitCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    //SHOW CLIENTS BY ID
    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }

    //SHOW ALL CLIENTS
    public Page<Client> getAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable);
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

    public List<Client> searchClients(String keyword) {
        return clientRepository
                .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContainingIgnoreCase(
                        keyword, keyword, keyword);
    }
}
