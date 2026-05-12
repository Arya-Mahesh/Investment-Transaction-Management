package com.rinar.trasactional_mgmt.controller;

import com.rinar.trasactional_mgmt.dto.LoginRequest;
import com.rinar.trasactional_mgmt.dto.RegisterRequest;
import com.rinar.trasactional_mgmt.repo.UserRepository;
import com.rinar.trasactional_mgmt.service.AuthService;
import com.rinar.trasactional_mgmt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public String Login(@RequestBody LoginRequest loginRequest){
        
        return authService.login(loginRequest);
    }
};
