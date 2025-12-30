package com.disputetrackingsystem.service;

import com.disputetrackingsystem.model.Client;
import com.disputetrackingsystem.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import org.springframework.lang.NonNull;

@Service
@Validated
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    // CREATE CLIENT
    public Client saveClient(@NonNull Client client) {
        return clientRepository.save(client);
    }

    // SHOW CLIENTS BY ID
    public Client getClientById(@NonNull Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }

    // SHOW ALL CLIENTS
    public Page<Client> getAllClients(@NonNull Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    // UPDATE CLIENTS
    public Client updateClient(@NonNull Client client) {
        return clientRepository.save(client);
    }

    // DELETE CLIENT
    public void deleteClient(@NonNull Long id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Client not found");
        }
        clientRepository.deleteById(id);
    }

    public List<Client> searchClients(@NonNull String keyword) {
        return clientRepository
                .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContainingIgnoreCase(
                        keyword, keyword, keyword);
    }
}
