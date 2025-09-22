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
        private String username;
        private Set<String> roles;

    public UserDTO(Long id, String name, String email, String username, Set<String> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.roles = roles;
    }
}
