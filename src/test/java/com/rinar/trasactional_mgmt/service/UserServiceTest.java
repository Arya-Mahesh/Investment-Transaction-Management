package com.rinar.trasactional_mgmt.service;

import com.rinar.trasactional_mgmt.dto.RegisterRequest;
import com.rinar.trasactional_mgmt.exception.BadRequestException;
import com.rinar.trasactional_mgmt.model.User;
import com.rinar.trasactional_mgmt.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void register_shouldSaveUser_whenUsernameIsNew() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("arya");
        request.setPassword("pass123");

        when(userRepository.findByUsername("arya")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass123")).thenReturn("encodedPass");

        userService.register(request);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_shouldThrowException_whenUsernameAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("arya");
        request.setPassword("pass123");

        when(userRepository.findByUsername("arya"))
                .thenReturn(Optional.of(new User(1L, "arya", "encoded", "USER")));

        assertThrows(BadRequestException.class, () -> userService.register(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_shouldAlwaysAssignUserRole_regardlessOfInput() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("hacker");
        request.setPassword("pass");

        when(userRepository.findByUsername("hacker")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass")).thenReturn("encoded");

        userService.register(request);

        // Verify the saved user always gets USER role, never ADMIN
        verify(userRepository).save(argThat(user -> "USER".equals(user.getRole())));
    }
}
