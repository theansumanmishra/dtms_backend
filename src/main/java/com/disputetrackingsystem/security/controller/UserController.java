package com.disputetrackingsystem.security.controller;

import com.disputetrackingsystem.DTO.UserDTO;
import com.disputetrackingsystem.security.model.Role;
import com.disputetrackingsystem.security.model.User;
import com.disputetrackingsystem.security.model.UserPrinciple;
import com.disputetrackingsystem.security.repository.UserRepository;
import com.disputetrackingsystem.security.service.CustomUserDetailsService;
import com.disputetrackingsystem.security.service.UserService;
import com.disputetrackingsystem.service.DisputeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DisputeService disputeService;

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

    //GET DISPUTE STATS FOR USER
    @GetMapping("/my-stats")
    public ResponseEntity<Map<String, Long>> getMyDisputeStats() {
        UserPrinciple currentUser = (UserPrinciple) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Long userId = currentUser.getId();

        Map<String, Long> stats = disputeService.getDisputeStatsForCurrentUser(userId);
        return ResponseEntity.ok(stats);
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

    @GetMapping("users/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        UserDTO userDto = new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUsername(),
                roles
        );

        return ResponseEntity.ok(userDto);
    }

    //LOGIN USER
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        String token = userService.verify(user);
        return ResponseEntity.ok("{\"accessToken\":\"" + token + "\"}");
    }
}
