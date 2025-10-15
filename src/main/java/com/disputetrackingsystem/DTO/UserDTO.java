package com.disputetrackingsystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
        private Long id;
        private String name;
        private String email;
        private Long phone;
        private String profilePhoto;
        private String username;
        private Boolean enabled = true;
        private Set<String> roles;
}
