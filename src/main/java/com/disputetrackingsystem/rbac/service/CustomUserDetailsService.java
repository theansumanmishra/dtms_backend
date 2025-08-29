package com.disputetrackingsystem.rbac.service;

import com.disputetrackingsystem.rbac.model.Permission;
import com.disputetrackingsystem.rbac.model.Role;
import com.disputetrackingsystem.rbac.model.User;
import com.disputetrackingsystem.rbac.model.UserPrinciple;
import com.disputetrackingsystem.rbac.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

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
