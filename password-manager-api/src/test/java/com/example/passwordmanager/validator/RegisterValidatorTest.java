package com.example.passwordmanager.validator;

import com.example.passwordmanager.dto.request.auth.RegisterRequest;
import com.example.passwordmanager.exception.password.PasswordException;
import com.example.passwordmanager.exception.RegisterException;
import com.example.passwordmanager.model.Role;
import com.example.passwordmanager.model.User;
import com.example.passwordmanager.repo.user.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterValidatorTest {
    @Mock
    private UserRepo repository;
    @Mock
    private EmailValidator emailValidator;
    @Mock
    private PasswordValidator passwordValidator;
    @InjectMocks
    private RegisterValidator registerValidator;

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

    private RegisterRequest mockRegisterRequest() {
        return RegisterRequest.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("Password!123")
                .build();
    }

    private void mockEmailValidator(String email) throws RegisterException {
        doNothing().when(emailValidator).validate(email);
    }

    private void mockPasswordValidator(String password) throws PasswordException {
        doNothing().when(passwordValidator).validate(password);
    }

    private void mockUserRepo(User user) {
        when(repository.save(user)).thenReturn(user);
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateRegister() throws RegisterException, PasswordException {
        // Arrange
        RegisterRequest registerRequest = mockRegisterRequest();

        mockEmailValidator(registerRequest.getEmail());
        mockPasswordValidator(registerRequest.getPassword());

        // Assert
        assertDoesNotThrow(() -> registerValidator.validate(registerRequest));
    }

    @Test
    void testValidateWhenEmailNotValid() throws RegisterException {
        // Arrange
        RegisterRequest registerRequest = mockRegisterRequest();
        String email = registerRequest.getEmail();

        doThrow(RegisterException.class).when(emailValidator).validate(email);

        // Assert
        assertThrows(RegisterException.class, () -> registerValidator.validate(registerRequest));
    }

    @Test
    void testValidateWhenEmailIsTaken() throws RegisterException {
        // Arrange
        User user = mockUser();
        RegisterRequest registerRequest = mockRegisterRequest();
        String email = registerRequest.getEmail();

        mockEmailValidator(email);
        mockUserRepo(user);

        // Assert
        assertThrows(RegisterException.class, () -> registerValidator.validate(registerRequest));
    }

    @Test
    void testValidateWhenPasswordNotValid() throws RegisterException, PasswordException {
        // Arrange
        RegisterRequest registerRequest = mockRegisterRequest();
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();

        mockEmailValidator(email);
        doThrow(PasswordException.class).when(passwordValidator).validate(password);


        // Assert
        assertThrows(PasswordException.class, () -> registerValidator.validate(registerRequest));
    }
}
