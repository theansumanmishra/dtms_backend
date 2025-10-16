package com.disputetrackingsystem.DTO;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String token;
    private String tempPassword;
    private String newPassword;
}
