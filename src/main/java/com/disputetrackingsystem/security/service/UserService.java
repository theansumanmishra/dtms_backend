package com.disputetrackingsystem.security.service;

import com.disputetrackingsystem.DTO.AuthResponse;
import com.disputetrackingsystem.security.model.Role;
import com.disputetrackingsystem.security.model.User;
import com.disputetrackingsystem.security.repository.RoleRepository;
import com.disputetrackingsystem.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    //REGISTER USERS
    public User Register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        // Attach roles properly
        Set<Role> managedRoles = new HashSet<>();
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            for (Role r : user.getRoles()) {
                Role managedRole = roleRepository.findById(r.getId())
                        .orElseThrow(() -> new RuntimeException("Role not found: " + r.getId()));
                managedRoles.add(managedRole);
            }
            user.setRoles(managedRoles);
        }
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

    // UPDATE USER
    public User updateUser(Long id, User userInput) {
        // Fetch existing user
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));

        // Update fields
        existingUser.setName(userInput.getName());
        existingUser.setEmail(userInput.getEmail());
        existingUser.setPhone(userInput.getPhone());
        existingUser.setUsername(userInput.getUsername());

        // Only update password if provided
        if (userInput.getPassword() != null && !userInput.getPassword().isEmpty()) {
            existingUser.setPassword(encoder.encode(userInput.getPassword()));
        }

        // Update roles only if provided in request
        if (userInput.getRoles() != null && !userInput.getRoles().isEmpty()) {
            Set<Role> managedRoles = new HashSet<>();
            for (Role r : userInput.getRoles()) {
                Role managedRole = roleRepository.findById(r.getId())
                        .orElseThrow(() -> new RuntimeException("Role not found: " + r.getId()));
                managedRoles.add(managedRole);
            }
            existingUser.setRoles(managedRoles);
        }
        // else → keep old roles unchanged

        return userRepository.save(existingUser);
    }

    //AUTHENTICATE USER
    public AuthResponse verify(User user) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()          //gives me the authentication object if auth is successful
                ));
        if (authentication.isAuthenticated()) {
            // return generated token
            String token = jwtService.generateToken(user.getUsername());

            // Extract the first role only, ignore permissions
            String role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(auth -> auth.startsWith("ROLE_"))  // only roles
                    .map(auth -> auth.replace("ROLE_", ""))    // remove ROLE_ prefix
                    .findFirst()
                    .orElse("USER");                           // default fallback

            return new AuthResponse(token, role);
        }
        throw new RuntimeException("Authentication failed");
    }
}
