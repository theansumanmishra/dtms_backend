package com.disputetrackingsystem.controller;

import com.disputetrackingsystem.entity.Client;
import com.disputetrackingsystem.repository.ClientRepository;
import com.disputetrackingsystem.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('MANAGER')")
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

    //CREATE MULTIPLE CLIENTS
//    @PostMapping
//    public ResponseEntity<List<Client>> createClients(@RequestBody List<Client> clients){
//        List<Client> savedClients = clientService.saveClients(clients);
//        return ResponseEntity.ok(savedClients);
//    }

    //SHOW CLIENT BY ID
    @PreAuthorize("hasAuthority('VIEW_CLIENT')")
    @GetMapping("{id}")
    public Client getClient(@PathVariable Long id) {
        return clientService.getClientById(id);
    }

    //SHOW ALL CLIENTS
    @PreAuthorize("hasAuthority('VIEW_CLIENT')")
    @GetMapping
    public List<Client> showAllClients() {
        return clientService.getAllClients();
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

    //FIND CLIENT BY NAME
    @GetMapping("/name/{name}")
    public ResponseEntity<Client> getClientByName(@PathVariable String name){
        Client client = clientService.findByClientName(name);
        return ResponseEntity.ok(client);
    }

    //FIND CLIENT BY PHONE NUMBER
    @GetMapping("/phone/{phone}")
    public ResponseEntity<Client> findClientByPhone(@PathVariable String phone){
        return ResponseEntity.ok(clientService.findClientByPhone(phone));
    }
}
