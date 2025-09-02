package com.disputetrackingsystem.rbac.service;

import com.disputetrackingsystem.rbac.model.User;
import com.disputetrackingsystem.rbac.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    //REGISTER USERS
    public User Register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    //GET USER BY ID
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    //GET ALL USERS
    public List<User> getUserList() {
        return userRepository.findAll();
    }

    public String verify(User user) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(), user.getPassword()          //gives me the authentication object if auth is successful
                ));
        if (authentication.isAuthenticated())
            return jwtService.generateToken(user.getUsername());

//            If authentication is successful, return a success message
//            return "User authenticated successfully";

        return "Authentication failed";
    }
}
