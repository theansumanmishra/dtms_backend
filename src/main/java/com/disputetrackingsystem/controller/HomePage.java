package com.disputetrackingsystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomePage {
    @GetMapping("/")
    public String Greet(){
        return "Welcome to my Project on Dispute Tracking and Management System";
    }
}
