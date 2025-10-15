package com.disputetrackingsystem.service;

import com.disputetrackingsystem.DTO.AuthResponse;
import com.disputetrackingsystem.model.Role;
import com.disputetrackingsystem.model.User;
import com.disputetrackingsystem.repository.RoleRepository;
import com.disputetrackingsystem.repository.UserRepository;
import com.disputetrackingsystem.security.service.JWTService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    private EmailService emailService;

    private final String uploadDir = "uploads/profile-photos/";

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    //REGISTER USERS WITH EMAIL FUNCTIONALITY
    public User Register(User user) {
        // 1️⃣ Generate random temp password
        String tempPassword = RandomStringUtils.randomAlphanumeric(10);

        // 2️⃣ Encode and set
        user.setPassword(encoder.encode(tempPassword));

        // 3️⃣ Generate reset token
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setTokenExpiry(LocalDateTime.now().plusHours(24));

        // 4️⃣ Attach roles properly
        Set<Role> managedRoles = new HashSet<>();
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            for (Role r : user.getRoles()) {
                Role managedRole = roleRepository.findById(r.getId())
                        .orElseThrow(() -> new RuntimeException("Role not found: " + r.getId()));
                managedRoles.add(managedRole);
            }
            user.setRoles(managedRoles);
        }

        // 5️⃣ Save
        User savedUser = userRepository.save(user);

        // 6️⃣ Send Email
        String resetLink = "http://localhost:8080/reset-password?token=" + token;
        String message = """
                Hi %s,
                
                Your account has been created successfully.
                Temporary password: %s
                
                Please reset your password using the link below:
                %s
                
                Your reset link will only be available for 1hr only. 
                Make sure to change within stipulated time period.
                """.formatted(user.getName(), tempPassword, resetLink);

        emailService.sendEmail(user.getEmail(), "Reset your password", message);

        return savedUser;
    }

    //GET USER BY ID
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    //GET ALL USERS
    public List<User> getUserList() {
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
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

        // Update "enabled" field if provided (admin can disable/enable user)
        if (userInput.getEnabled() != null) {
            existingUser.setEnabled(userInput.getEnabled());
        }
        return userRepository.save(existingUser);
    }

    //AUTHENTICATE USERNAME & PASSWORD
    public AuthResponse verify(User user) {

        // 1️⃣ Authenticate user
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()          //gives me the authentication object if auth is successful
                )
        );

        // 2️⃣ If authentication is successful, generate token
        if (authentication.isAuthenticated()) {
            // return generated token
            String token = jwtService.generateToken(user.getUsername());

            // Extract the first role only, ignore permissions
            String role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(auth -> auth.startsWith("ROLE_"))                    // only roles
                    .map(auth -> auth.replace("ROLE_", ""))
                    .findFirst()
                    .orElse("USER");                                            // default fallback

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

    //DELETE PIC
    public void deleteUserPhoto(Long userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Optional: delete file from file system
        if (user.getProfilePhoto() != null) {
            String photoPath = "uploads" + user.getProfilePhoto(); // adjust if full path stored
            File file = new File(photoPath);
            if (file.exists()) {
                file.delete(); // deletes file from disk
            }
        }

        // Remove reference in DB
        user.setProfilePhoto(null);
        userRepository.save(user);
    }

    //SENDING RESET PASSWORD LINK
    public void sendResetLink(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User with given email does not exist");
        }

        User user = userOpt.get();

        // Generate reset token and expiry time
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setTokenExpiry(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        // Frontend reset link (where user will reset password)
        String resetLink = "http://localhost:3000/reset-password?token=" + token;

        // Email message content
        String message = String.format("""
                Hi %s,
                
                We received a request to reset your password.
                Click the link below to set a new password:
                
                %s
                
                This link will expire in 15 minutes.
                
                - Dispute Tracking System
                """, user.getName(), resetLink);

        emailService.sendEmail(
                user.getEmail(),
                "Password Reset Request",
                message
        );
    }
}
