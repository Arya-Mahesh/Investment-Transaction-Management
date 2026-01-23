package com.rinar.trasactional_mgmt.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
