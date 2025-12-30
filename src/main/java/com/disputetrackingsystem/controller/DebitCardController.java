package com.disputetrackingsystem.controller;

import com.disputetrackingsystem.model.DebitCard;
import com.disputetrackingsystem.service.DebitCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.lang.NonNull;

import java.util.List;

@RestController
@RequestMapping("/debitcards")
public class DebitCardController {

    @Autowired
    private DebitCardService debitCardService;

    // CREATE DEBIT CARDS ##
    @PostMapping
    public DebitCard postDebitCard(@RequestBody DebitCard debitCard) {
        return debitCardService.createDebitCard(debitCard);
    }

    // SHOW ALL DEBIT CARDS ##
    @GetMapping
    public List<DebitCard> getAllDebitCards() {
        return debitCardService.showAllDebitCards();
    }

    // GET DEBIT CARD BY ID ##
    @GetMapping("/{id}")
    public DebitCard getDebitCardById(@PathVariable @NonNull Long id) {
        return debitCardService.getDebitById(id);
    }

}
