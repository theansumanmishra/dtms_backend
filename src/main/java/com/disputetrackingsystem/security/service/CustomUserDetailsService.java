package com.disputetrackingsystem.security.service;

import com.disputetrackingsystem.model.User;
import com.disputetrackingsystem.security.model.UserPrinciple;
import com.disputetrackingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)         //this gets the userDetails from DB stores in user.
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        //there is no class that implements UserDetails Interface, so idea is to create Own Class that implements it
        return new UserPrinciple(user);
    }
}
