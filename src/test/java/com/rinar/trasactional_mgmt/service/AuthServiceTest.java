package com.rinar.trasactional_mgmt.service;

import com.rinar.trasactional_mgmt.dto.LoginRequest;
import com.rinar.trasactional_mgmt.exception.BadRequestException;
import com.rinar.trasactional_mgmt.model.User;
import com.rinar.trasactional_mgmt.repo.UserRepository;
import com.rinar.trasactional_mgmt.security.JWTUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid(){
        User user = new User(1L, "arya", "encodedPass", "USER");
        LoginRequest request = new LoginRequest();
        request.setUsername("arya");
        request.setPassword("rawPass");

        when(userRepository.findByUsername("arya")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("rawPass", "encodedPass")).thenReturn(true);
        when(jwtUtil.generateToken("arya","USER")).thenReturn("mocked.jwt.token");

        String token = authService.login(request);

        assertEquals("mocked.jwt.token", token);
        verify(jwtUtil, times(1)).generateToken("arya", "USER");

    }
    @Test
    void login_shouldThrowException_whenUsernameNotFound() {
        LoginRequest request = new LoginRequest();
        request.setUsername("unknown");
        request.setPassword("pass");

        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> authService.login(request));
        verify(jwtUtil, never()).generateToken(any(), any());
    }

    @Test
    void login_shouldThrowException_whenPasswordIsWrong() {
        User user = new User(1L, "arya", "encodedPass", "USER");
        LoginRequest request = new LoginRequest();
        request.setUsername("arya");
        request.setPassword("wrongPass");

        when(userRepository.findByUsername("arya")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPass", "encodedPass")).thenReturn(false);

        assertThrows(BadRequestException.class, () -> authService.login(request));
        verify(jwtUtil, never()).generateToken(any(), any());
    }

}
