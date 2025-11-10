package com.disputetrackingsystem.controller;

import com.disputetrackingsystem.model.Client;
import com.disputetrackingsystem.repository.ClientRepository;
import com.disputetrackingsystem.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    //CREATE CLIENT
    @PreAuthorize("hasAuthority('CREATE_CLIENT')")
    @PostMapping
    public Client createClient(@RequestBody Client client) {
        return clientService.saveClient(client);
    }

    //SHOW CLIENT BY ID
    @PreAuthorize("hasAuthority('VIEW_CLIENT')")
    @GetMapping("{id}")
    public Client getClient(@PathVariable Long id) {
        return clientService.getClientById(id);
    }

    //SHOW ALL CLIENTS
    @PreAuthorize("hasAuthority('VIEW_CLIENT')")
    @GetMapping
    public ResponseEntity<Page<Client>> getAllClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Client> clients = clientService.getAllClients(pageable);
        return ResponseEntity.ok(clients);
    }

    //UPDATE CLIENTS
    @PreAuthorize("hasAuthority('UPDATE_CLIENT')")
    @PutMapping
    public Client updateClient(@RequestBody Client client) {
        return clientService.saveClient(client);
    }

    //DELETE CLIENT
    @PreAuthorize("hasAuthority('MANAGE_CLIENT')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteClient(Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok("Client with ID " + id + " has been deleted.");
    }

    // API for live search / autocomplete
    @PreAuthorize("hasAuthority('VIEW_CLIENT')")
    @GetMapping("/search")
    public ResponseEntity<List<Client>> searchClients(@RequestParam String keyword) {
        List<Client> clients = clientService.searchClients(keyword);
        return ResponseEntity.ok(clients);
    }
}
