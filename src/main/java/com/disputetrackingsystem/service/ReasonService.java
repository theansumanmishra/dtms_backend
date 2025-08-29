package com.disputetrackingsystem.service;

import com.disputetrackingsystem.entity.Reason;
import com.disputetrackingsystem.repository.ReasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReasonService {

    @Autowired
    private ReasonRepository reasonRepository;

    public List<Reason> getAllReasons(){
        return reasonRepository.findAll();
    }
}
