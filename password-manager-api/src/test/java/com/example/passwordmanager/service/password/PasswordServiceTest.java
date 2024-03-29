package com.example.passwordmanager.service.password;

import com.example.passwordmanager.crypto.Encrypter;
import com.example.passwordmanager.dto.request.password.CreatePasswordRequest;
import com.example.passwordmanager.dto.response.password.CreatePasswordResponse;
import com.example.passwordmanager.dto.response.password.GetAllPasswordsResponse;
import com.example.passwordmanager.dto.response.password.GetPasswordResponse;
import com.example.passwordmanager.dto.util.BasicPassword;
import com.example.passwordmanager.exception.AuthException;
import com.example.passwordmanager.exception.ExceptionMessages;
import com.example.passwordmanager.exception.password.PasswordCreationException;
import com.example.passwordmanager.exception.password.PasswordException;
import com.example.passwordmanager.model.Password;
import com.example.passwordmanager.model.Role;
import com.example.passwordmanager.model.User;
import com.example.passwordmanager.repo.user.PasswordRepo;
import com.example.passwordmanager.validator.AuthValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PasswordServiceTest {
    @Mock
    private PasswordRepo passwordRepo;
    @Mock
    private AuthValidator authValidator;
    @Mock
    private Encrypter passwordEncrypter;
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
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .isLocked(false)
                .build();
    }

    private Password mockPassword(Long id, User owner, String encryptedValue) {
        return Password.builder()
                .id(id)
                .name("Password")
                .encryptedValue(encryptedValue)
                .owner(owner)
                .build();
    }

    private void mockAuthValidator(User user) {
        when(authValidator.validateUser()).thenReturn(user);
    }

    private void mockPasswordRepoFindById(Long id, Password password) {
        when(passwordRepo.findById(id)).thenReturn(Optional.ofNullable(password));
    }

    private void mockPasswordRepoFindAllByOwner(User owner, List<Password> passwords) {
        when(passwordRepo.findAllByOwner(owner)).thenReturn(passwords);
    }

    private void mockPasswordEncrypter(String password, String encryptedPassword) throws GeneralSecurityException {
        when(passwordEncrypter.encrypt(password)).thenReturn(encryptedPassword);
        when(passwordEncrypter.decrypt(encryptedPassword)).thenReturn(password);
    }

    @Test
    void testGetPasswordResponse() throws PasswordException, GeneralSecurityException {
        // Arrange
        User owner = mockUser();
        String plainPassword = "Password!123";
        String encryptedPassword = "abcdefghij";
        Password password = mockPassword(1L, owner, encryptedPassword);
        mockAuthValidator(owner);
        mockPasswordRepoFindById(password.getId(), password);
        mockPasswordEncrypter(plainPassword, encryptedPassword);

        // Act
        GetPasswordResponse response = passwordService.getPasswordResponse(password.getId());

        // Assert
        assertNotNull(response);
        assertEquals(response.getPassword(), new BasicPassword(password, plainPassword));

    }

    @Test
    void testGetPasswordResponseWhenAuthFail() {
        // Arrange
        User owner = mockUser();
        String plainPassword = "Password!123";
        String encryptedPassword = "abcdefghij";
        Password password = mockPassword(1L, owner, encryptedPassword);
        when(authValidator.validateUser()).thenThrow(AuthException.class);
        mockPasswordRepoFindById(password.getId(), password);

        // Assert
        assertThrows(AuthException.class, () -> passwordService.getPasswordResponse(password.getId()));

    }

    @Test
    void testGetPasswordResponseWhenUserNotFound() {
        // Arrange
        User owner = mockUser();
        String plainPassword = "Password!123";
        String encryptedPassword = "abcdefghij";
        Password password = mockPassword(1L, owner, encryptedPassword);
        when(authValidator.validateUser()).thenThrow(UsernameNotFoundException.class);
        mockPasswordRepoFindById(password.getId(), password);

        // Assert
        assertThrows(UsernameNotFoundException.class, () -> passwordService.getPasswordResponse(password.getId()));
    }

    @Test
    void testGetPasswordResponseWhenPasswordNotFound() {
        // Arrange
        User owner = mockUser();
        String plainPassword = "Password!123";
        String encryptedPassword = "abcdefghij";
        Password password = mockPassword(1L, owner, encryptedPassword);
        mockAuthValidator(owner);
        mockPasswordRepoFindById(password.getId(), null);

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
        String plainPassword = "Password!123";
        String encryptedPassword = "abcdefghij";

        Password password = mockPassword(1L, owner, encryptedPassword);
        mockAuthValidator(requester);
        mockPasswordRepoFindById(password.getId(), password);

        // Assert
        Exception exception = assertThrows(PasswordException.class, () -> passwordService.getPasswordResponse(password.getId()));
        assertEquals(exception.getMessage(), ExceptionMessages.getPasswordWithIdOwnedByNotFound(owner.getEmail(), password.getId()).getMessage());
    }

    @Test
    void testGetAllPasswordsResponse() throws GeneralSecurityException {
        // Arrange
        User owner = mockUser();
        String plainPassword1 = "Password!123";
        String encryptedPassword1 = "abcdefghij";
        String plainPassword2 = "Password!1234";
        String encryptedPassword2 = "xyzcvf";

        Password password1 = mockPassword(1L, owner, encryptedPassword1);
        Password password2 = mockPassword(2L, owner, encryptedPassword2);
        mockAuthValidator(owner);
        mockPasswordRepoFindAllByOwner(owner, List.of(password1, password2));
        mockPasswordEncrypter(plainPassword1, encryptedPassword1);
        mockPasswordEncrypter(plainPassword2, encryptedPassword2);
        // Act
        GetAllPasswordsResponse response = passwordService.getAllPasswordsResponse();

        // Assert
        assertNotNull(response);
        assertTrue(response.getPasswords().contains(new BasicPassword(password1, plainPassword1)));
        assertTrue(response.getPasswords().contains(new BasicPassword(password2, plainPassword2)));
    }

    @Test
    void testGetAllPasswordsResponseWhenNoPasswordFound() {
        // Arrange
        User owner = mockUser();
        mockAuthValidator(owner);
        mockPasswordRepoFindAllByOwner(owner, List.of());

        // Act
        GetAllPasswordsResponse response = passwordService.getAllPasswordsResponse();

        // Assert
        assertNotNull(response);
        assertTrue(response.getPasswords().isEmpty());
    }

    @Test
    void testGetAllPasswordsResponseWhenAuthFail() {
        // Arrange
        User owner = mockUser();
        String plainPassword1 = "Password!123";
        String encryptedPassword1 = "abcdefghij";
        String plainPassword2 = "Password!1234";
        String encryptedPassword2 = "xyzcvf";

        Password password1 = mockPassword(1L, owner, encryptedPassword1);
        Password password2 = mockPassword(2L, owner, encryptedPassword2);
        when(authValidator.validateUser()).thenThrow(AuthException.class);
        mockPasswordRepoFindAllByOwner(owner, List.of(password1, password2));

        // Assert
        assertThrows(AuthException.class, () -> passwordService.getAllPasswordsResponse());
    }

    @Test
    void testGetAllPasswordsResponseWhenUserNotFound() {
        // Arrange
        User owner = mockUser();
        String plainPassword1 = "Password!123";
        String encryptedPassword1 = "abcdefghij";
        String plainPassword2 = "Password!1234";
        String encryptedPassword2 = "xyzcvf";

        Password password1 = mockPassword(1L, owner, encryptedPassword1);
        Password password2 = mockPassword(2L, owner, encryptedPassword2);
        when(authValidator.validateUser()).thenThrow(UsernameNotFoundException.class);
        mockPasswordRepoFindAllByOwner(owner, List.of(password1, password2));

        // Assert
        assertThrows(UsernameNotFoundException.class, () -> passwordService.getAllPasswordsResponse());
    }

    @Test
    void testCreatePasswordResponse() throws PasswordCreationException, GeneralSecurityException {
        // Arrange
        User owner = mockUser();
        String name = "password";
        String value = "value";
        String encryptedValue = "encryptedValue";
        CreatePasswordRequest request = mockCreatePasswordRequest(name, value);
        mockAuthValidator(owner);
        mockPasswordEncrypter(value, encryptedValue);

        // Act
        CreatePasswordResponse response = passwordService.createPasswordResponse(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getPassword());
        assertEquals(response.getPassword().getName(), name);
        assertEquals(response.getPassword().getDecryptedValue(), value);
        assertEquals(response.getPassword().getOwnerId(), owner.getId());
        assertNotNull(response.getPassword().getCreated());
        assertNotNull(response.getPassword().getModified());
    }

    @Test
    void testCreatePasswordResponseWhenAuthFail() {
        // Arrange
        User owner = mockUser();
        String name = "password";
        String value = "value";
        CreatePasswordRequest request = mockCreatePasswordRequest(name, value);
        when(authValidator.validateUser()).thenThrow(AuthException.class);

        // Assert
        assertThrows(AuthException.class, () -> passwordService.createPasswordResponse(request));

    }

    @Test
    void testCreatePasswordResponseWhenUserNotFound() {
        // Arrange
        User owner = mockUser();
        String name = "password";
        String value = "value";
        CreatePasswordRequest request = mockCreatePasswordRequest(name, value);
        when(authValidator.validateUser()).thenThrow(UsernameNotFoundException.class);

        // Assert
        assertThrows(UsernameNotFoundException.class, () -> passwordService.createPasswordResponse(request));
    }

    @ParameterizedTest
    @ValueSource(strings = {""})
    @NullSource
    void testCreatePasswordResponseWhenNameIsNotValid(String name) throws GeneralSecurityException {
        // Arrange
        User owner = mockUser();
        String value = "value";
        String encryptedValue = "encryptedValue";
        CreatePasswordRequest request = mockCreatePasswordRequest(name, value);
        mockAuthValidator(owner);
        mockPasswordEncrypter(value, encryptedValue);

        // Assert
        Exception ex = assertThrows(PasswordException.class, () -> passwordService.createPasswordResponse(request));
        assertEquals(ex.getMessage(), ExceptionMessages.getPasswordNameCannotBeNull(owner.getEmail()).getMessage());
    }

    @Test
    void testDeletePassword() {
        // Arrange
        User owner = mockUser();
        String plainPassword = "Password!123";
        String encryptedPassword = "abcdefghij";

        Password password = mockPassword(1L, owner, encryptedPassword);
        mockAuthValidator(owner);
        mockPasswordRepoFindById(password.getId(), password);

        // Assert
        assertDoesNotThrow(() -> passwordService.deletePassword(password.getId()));
    }

    @Test
    void testDeletePasswordWhenAuthFail() {
        // Arrange
        User owner = mockUser();
        String plainPassword = "Password!123";
        String encryptedPassword = "abcdefghij";

        Password password = mockPassword(1L, owner, encryptedPassword);
        when(authValidator.validateUser()).thenThrow(AuthException.class);
        mockPasswordRepoFindById(password.getId(), password);

        // Assert
        assertThrows(AuthException.class, () -> passwordService.deletePassword(password.getId()));
    }

    @Test
    void testDeletePasswordWhenUserNotFound() {
        // Arrange
        User owner = mockUser();
        String plainPassword = "Password!123";
        String encryptedPassword = "abcdefghij";

        Password password = mockPassword(1L, owner, encryptedPassword);
        when(authValidator.validateUser()).thenThrow(UsernameNotFoundException.class);
        mockPasswordRepoFindById(password.getId(), password);

        // Assert
        assertThrows(UsernameNotFoundException.class, () -> passwordService.deletePassword(password.getId()));
    }

    @Test
    void testDeletePasswordWhenPasswordNotFound() {
        // Arrange
        User owner = mockUser();
        String plainPassword = "Password!123";
        String encryptedPassword = "abcdefghij";

        Password password = mockPassword(1L, owner, encryptedPassword);
        mockAuthValidator(owner);
        mockPasswordRepoFindById(password.getId(), null);

        // Assert
        Exception exception = assertThrows(PasswordException.class, () -> passwordService.deletePassword(password.getId()));
        assertEquals(exception.getMessage(), ExceptionMessages.getPasswordWithIdNotFound(password.getId()));
    }

    @Test
    void testDeletePasswordWhenRequesterIsNotOwner() {
        // Arrange
        User requester = mockUser();
        User owner = mockUser();
        String plainPassword = "Password!123";
        String encryptedPassword = "abcdefghij";
        owner.setEmail("owner");

        Password password = mockPassword(1L, owner, encryptedPassword);
        mockAuthValidator(requester);
        mockPasswordRepoFindById(password.getId(), password);

        // Assert
        Exception exception = assertThrows(PasswordException.class, () -> passwordService.getPasswordResponse(password.getId()));
        assertEquals(exception.getMessage(), ExceptionMessages.getPasswordWithIdOwnedByNotFound(owner.getEmail(), password.getId()).getMessage());
    }
}
