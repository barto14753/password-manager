package com.example.passwordmanager.service.password;

import com.example.passwordmanager.dto.request.PatchProfileRequest;
import com.example.passwordmanager.dto.request.password.CreatePasswordRequest;
import com.example.passwordmanager.dto.response.password.GetPasswordResponse;
import com.example.passwordmanager.dto.util.BasicPassword;
import com.example.passwordmanager.exception.AuthException;
import com.example.passwordmanager.exception.ExceptionMessages;
import com.example.passwordmanager.exception.PasswordException;
import com.example.passwordmanager.model.Password;
import com.example.passwordmanager.model.Role;
import com.example.passwordmanager.model.User;
import com.example.passwordmanager.repo.user.PasswordRepo;
import com.example.passwordmanager.validator.AuthValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PasswordServiceTest {
    @Mock
    private PasswordRepo passwordRepo;
    @Mock
    private AuthValidator authValidator;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private PasswordService passwordService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private CreatePasswordRequest mockCreatePasswordRequest(String name, String value) {
        return CreatePasswordRequest.builder()
                .name(name)
                .value(value)
                .build();
    }

    private User mockUser() {
        return User.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .isLocked(false)
                .build();
    }

    private Password mockPassword(User owner) {
        return Password.builder()
                .id(1L)
                .name("Password")
                .value("Password!123")
                .owner(owner)
                .build();
    }

    private void mockAuthValidator(User user) {
        when(authValidator.validateUser()).thenReturn(user);
    }

    private void mockPasswordRepo(Long id, Password password) {
        when(passwordRepo.findById(id)).thenReturn(Optional.ofNullable(password));
    }

    @Test
    void testGetPasswordResponse() throws PasswordException {
        // Arrange
        User owner = mockUser();
        Password password = mockPassword(owner);
        mockAuthValidator(owner);
        mockPasswordRepo(password.getId(), password);

        // Act
        GetPasswordResponse response = passwordService.getPasswordResponse(password.getId());

        // Assert
        assertNotNull(response);
        assertEquals(response.getPassword(), new BasicPassword(password));

    }

    @Test
    void testGetPasswordResponseWhenAuthFail() {
        // Arrange
        User owner = mockUser();
        Password password = mockPassword(owner);
        when(authValidator.validateUser()).thenThrow(AuthException.class);
        mockPasswordRepo(password.getId(), password);

        // Assert
        assertThrows(AuthException.class, () -> passwordService.getPasswordResponse(password.getId()));

    }

    @Test
    void testGetPasswordResponseWhenUserNotFound() {
        // Arrange
        User owner = mockUser();
        Password password = mockPassword(owner);
        when(authValidator.validateUser()).thenThrow(UsernameNotFoundException.class);
        mockPasswordRepo(password.getId(), password);

        // Assert
        assertThrows(UsernameNotFoundException.class, () -> passwordService.getPasswordResponse(password.getId()));

    }

    @Test
    void testGetPasswordResponseWhenPasswordNotFound() {
        // Arrange
        User owner = mockUser();
        Password password = mockPassword(owner);
        mockAuthValidator(owner);
        mockPasswordRepo(password.getId(), null);

        // Assert
        Exception exception = assertThrows(PasswordException.class, () -> passwordService.getPasswordResponse(password.getId()));
        assertEquals(exception.getMessage(), ExceptionMessages.getPasswordWithIdNotFound(password.getId()));


    }

    @Test
    void testGetPasswordResponseWhenRequesterIsNotOwner() {
        // Arrange
        User requester = mockUser();
        User owner = mockUser();
        owner.setEmail("owner");

        Password password = mockPassword(owner);
        mockAuthValidator(requester);
        mockPasswordRepo(password.getId(), password);

        // Assert
        Exception exception = assertThrows(PasswordException.class, () -> passwordService.getPasswordResponse(password.getId()));
        assertEquals(exception.getMessage(), ExceptionMessages.getPasswordWithIdOwnedByNotFound(owner.getEmail(), password.getId()).getMessage());
    }

}
