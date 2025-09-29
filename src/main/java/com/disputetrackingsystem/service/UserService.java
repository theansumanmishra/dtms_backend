package com.disputetrackingsystem.service;

import com.disputetrackingsystem.DTO.AuthResponse;
import com.disputetrackingsystem.model.Role;
import com.disputetrackingsystem.model.User;
import com.disputetrackingsystem.repository.RoleRepository;
import com.disputetrackingsystem.repository.UserRepository;
import com.disputetrackingsystem.security.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

    private final String uploadDir = "uploads/profile-photos/";

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

    //UPLOAD PIC
    public String saveProfilePhoto(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create folder if not exists
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        // Save file
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir + fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        // Update user profile image URL
        String imageUrl = "/files/" + fileName; // public endpoint
        user.setProfilePhoto(imageUrl);
        userRepository.save(user);

        return imageUrl;
    }
}
