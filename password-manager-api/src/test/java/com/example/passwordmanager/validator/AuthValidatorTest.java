package com.example.passwordmanager.validator;

import com.example.passwordmanager.exception.AuthException;
import com.example.passwordmanager.model.Role;
import com.example.passwordmanager.model.User;
import com.example.passwordmanager.repo.user.UserRepo;
import com.example.passwordmanager.service.auth.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AuthValidatorTest {
    @Mock
    private UserRepo repository;
    @Mock
    private AuthenticationService authService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthValidator authValidator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
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

    private void mockUserRepo(User user) {
        when(repository.save(user)).thenReturn(user);
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    }

    private void mockAuthenticationService(String email) {
        when(authentication.getName()).thenReturn(email);
        when(authService.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void testValidateUser() {
        // Arrange
        User user = mockUser();
        mockAuthenticationService(user.getEmail());
        mockUserRepo(user);

        // Act
        User resultUser = authValidator.validateUser();

        // Assert
        assertNotNull(resultUser);
        assertEquals(user, resultUser);
    }

    @Test
    void testValidateUserWhenAuthFail() {
        // Arrange
        User user = mockUser();
        mockUserRepo(user);

        // Assert
        assertThrows(AuthException.class, () -> authValidator.validateUser());
    }

    @Test
    void testValidateUserWhenUserNotFound() {
        // Arrange
        User user = mockUser();
        mockAuthenticationService(user.getEmail());

        // Assert
        assertThrows(UsernameNotFoundException.class, () -> authValidator.validateUser());
    }
}
