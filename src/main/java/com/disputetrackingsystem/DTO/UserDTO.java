package com.disputetrackingsystem.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDTO {
        private Long id;
        private String name;
        private String email;
        private Long phone;
        private String profilePhoto;
        private String username;
        private Set<String> roles;

    public UserDTO(Long id, String name, String email, Long phone, String profilePhoto, String username, Set<String> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.profilePhoto = profilePhoto;
        this.username = username;
        this.roles = roles;
    }
}
