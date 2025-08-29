package com.disputetrackingsystem.rbac.service;

import com.disputetrackingsystem.rbac.model.User;
import com.disputetrackingsystem.rbac.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    //CREATE USERS
    public User saveUser(User user){
        return userRepository.save(user);
    }

    //GET USER BY ID
    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow();
    }

    //GET ALL USERS
    public List<User> getUserList(){
        return userRepository.findAll();
    }
}
