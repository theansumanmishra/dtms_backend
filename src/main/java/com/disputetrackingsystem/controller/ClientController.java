package com.disputetrackingsystem.controller;

import com.disputetrackingsystem.entity.Client;
import com.disputetrackingsystem.entity.DebitCard;
import com.disputetrackingsystem.repository.ClientRepository;
import com.disputetrackingsystem.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('MANAGER') and hasAuthority('READ_REPORT')")
@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    //CREATE CLIENTS ##
    @PostMapping
    public Client createClient(@RequestBody Client client){
        return clientService.saveClient(client);
    }

    //SHOW CLIENT BY ID
    @GetMapping("/{id}")
    public Client getEachClient(@PathVariable Long id){
        return clientService.getClientById(id);
    }

    //SHOW ALL CLIENTS ##
    @GetMapping
    public List<Client> showAllClients(){
        return clientService.getAllClients();
    }

    //UPDATE CLIENTS ##
    @PutMapping
    public Client updateClient(@RequestBody Client client){
        return clientService.saveClient(client);
    }

}
