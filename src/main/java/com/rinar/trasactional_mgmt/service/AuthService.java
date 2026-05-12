package com.rinar.trasactional_mgmt.service;

import com.rinar.trasactional_mgmt.dto.LoginRequest;
import com.rinar.trasactional_mgmt.exception.BadRequestException;
import com.rinar.trasactional_mgmt.model.User;
import com.rinar.trasactional_mgmt.repo.UserRepository;
import com.rinar.trasactional_mgmt.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }


    public String login(LoginRequest loginRequest){
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(()->  new BadRequestException("Invalid username or password"));

        if(!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())){
            throw new BadRequestException("Invalid username or password");

        }

     return jwtUtil.generateToken(user.getUsername(),user.getRole());
    }
}
