package com.disputetrackingsystem.controller;

import com.disputetrackingsystem.entity.DebitCard;
import com.disputetrackingsystem.repository.DebitCardRepository;
import com.disputetrackingsystem.service.DebitCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('MANAGER')")
@RestController
@RequestMapping("/debitcards")
public class DebitCardController {

    @Autowired
    private DebitCardService debitCardService;

    @Autowired
    private DebitCardRepository debitCardRepository;

    //CREATE DEBIT CARDS ##
    @PostMapping
    public DebitCard postDebitCard(@RequestBody DebitCard debitCard) {
        return debitCardService.createDebitCard(debitCard);
    }

    //SHOW ALL DEBIT CARDS ##
    @GetMapping
    public List<DebitCard> getAllDebitCards() {
        return debitCardService.showAllDebitCards();
    }

    //GET DEBIT CARD BY ID ##
    @GetMapping("/{id}")
    public DebitCard getDebitById(@PathVariable Long id) {
        return debitCardService.getDebitById(id);
    }

}
