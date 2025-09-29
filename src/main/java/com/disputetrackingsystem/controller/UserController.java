package com.disputetrackingsystem.controller;

import com.disputetrackingsystem.DTO.AuthResponse;
import com.disputetrackingsystem.DTO.UserDTO;
import com.disputetrackingsystem.model.Role;
import com.disputetrackingsystem.model.User;
import com.disputetrackingsystem.security.model.UserPrinciple;
import com.disputetrackingsystem.repository.UserRepository;
import com.disputetrackingsystem.service.UserService;
import com.disputetrackingsystem.service.DisputeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DisputeService disputeService;

    //CREATE USER
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
        return userService.updateUser(id, user);
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

    //GET INDIVIDUAL USER
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
                user.getPhone(),
                user.getProfilePhoto(),
                user.getUsername(),
                roles
        );

        return ResponseEntity.ok(userDto);
    }

    //LOGIN USER
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) {
        AuthResponse response = userService.verify(user);
        return ResponseEntity.ok(response);
    }

    //UPLOAD PIC
    @PostMapping("users/{userId}/upload-photo")
    public ResponseEntity<String> uploadProfilePhoto(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {

        try {
            String imageUrl = userService.saveProfilePhoto(userId, file);
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload: " + e.getMessage());
        }
    }
}
