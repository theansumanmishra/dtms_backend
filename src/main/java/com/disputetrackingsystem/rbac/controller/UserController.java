package com.disputetrackingsystem.rbac.controller;

import com.disputetrackingsystem.rbac.model.User;
import com.disputetrackingsystem.rbac.repository.UserRepository;
import com.disputetrackingsystem.rbac.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    //CREATE USER
    @PreAuthorize("hasAuthority('CREATE_USER')")  //only user with CREATE_USER authority can create new user
    @PostMapping
    public User createUser(@RequestBody User user){
        return userService.saveUser(user);
    }

    //SHOW ALL USER
    @GetMapping
    public List<User> getAllUsers(){
        return userService.getUserList();
    }

    //SHOW USER BY ID
    @GetMapping("/{id}")
    public User findUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }
}
