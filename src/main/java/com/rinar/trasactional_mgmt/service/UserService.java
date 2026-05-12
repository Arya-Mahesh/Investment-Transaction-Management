package com.rinar.trasactional_mgmt.service;

import com.rinar.trasactional_mgmt.dto.LoginRequest;
import com.rinar.trasactional_mgmt.dto.RegisterRequest;
import com.rinar.trasactional_mgmt.exception.BadRequestException;
import com.rinar.trasactional_mgmt.model.User;
import com.rinar.trasactional_mgmt.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void register(RegisterRequest registerRequest){
        if(userRepository.findByUsername(registerRequest.getUsername()).isPresent()){
            throw new BadRequestException("Username already exists");
        }
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
    }


}
