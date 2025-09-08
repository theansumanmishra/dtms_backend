package com.disputetrackingsystem.security.controller;

import com.disputetrackingsystem.security.model.User;
import com.disputetrackingsystem.security.repository.UserRepository;
import com.disputetrackingsystem.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    //REGISTER USER
    @PreAuthorize("hasAuthority('CREATE_USER')")  //only user with CREATE_USER authority can create new user
    @PostMapping("/register")
    public User createUser(@RequestBody User user) {
        return userService.Register(user);
    }

    //SHOW ALL USER
    @PreAuthorize("hasAuthority('VIEW_USER')")  //only user with CREATE_USER authority can create new user
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getUserList();
    }

    //SHOW USER BY ID
    @PreAuthorize("hasAuthority('VIEW_USER')")
    @GetMapping("/users/{id}")
    public User findUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    //UPDATE USER
    @PreAuthorize("hasAuthority('UPDATE_USER')")
    @PutMapping("/users/{id}")
    public User updateUser(@RequestBody User user, @PathVariable Long id) {
        User existingUser = userService.getUserById(id);
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        existingUser.setRoles(user.getRoles());
        return userService.updateUser(existingUser);
    }

    //DELETE USER
    @PreAuthorize("hasAuthority('MANAGE_USER')")
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
        return "User deleted successfully";
    }

    //LOGIN USER
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        String token = userService.verify(user);
        return ResponseEntity.ok("{\"accessToken\":\"" + token + "\"}");
    }
}
