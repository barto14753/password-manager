package com.example.passwordmanager.service;

import com.example.passwordmanager.config.jwt.JwtConfig;
import com.example.passwordmanager.config.jwt.JwtService;
import com.example.passwordmanager.dto.request.PatchProfileRequest;
import com.example.passwordmanager.dto.response.ProfileResponse;
import com.example.passwordmanager.exception.AuthException;
import com.example.passwordmanager.exception.ExceptionMessage;
import com.example.passwordmanager.model.Role;
import com.example.passwordmanager.model.User;
import com.example.passwordmanager.repo.user.UserRepo;
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

class ProfileServiceTest {
    @Mock
    private UserRepo repository;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ProfileService profileService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private PatchProfileRequest mockPatchProfileRequest(String newFirstName, String newLastName) {
        return PatchProfileRequest.builder()
                .firstName(newFirstName)
                .lastName(newLastName)
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

    private void mockAuthenticationService(String email) {
        when(authentication.getName()).thenReturn(email);
        when(authenticationService.getAuthentication()).thenReturn(authentication);
    }

    private void mockUserRepo(User user) {
        when(repository.save(user)).thenReturn(user);
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    }

    @Test
    void testGetProfile() {
        // Arrange
        User user = mockUser();
        mockAuthenticationService(user.getEmail());
        mockUserRepo(user);

        // Act
        ProfileResponse response = profileService.getProfile();

        // Assert
        assertNotNull(response);
        assertEquals(user.getEmail(), response.getProfile().getEmail());
        assertEquals(user.getFirstname(), response.getProfile().getFirstName());
        assertEquals(user.getLastname(), response.getProfile().getLastName());
    }

    @Test
    void testGetProfileWhenNullAuthentication() {
        // Arrange
        when(authenticationService.getAuthentication()).thenReturn(null);

        // Assert
        Exception exception = assertThrows(AuthException.class, () -> profileService.getProfile());
        assertEquals(ExceptionMessage.AUTH_FAILED, exception.getMessage());
    }

    @Test
    void testGetProfileWhenUserNotFound() {
        // Arrange
        User user = mockUser();
        mockAuthenticationService(user.getEmail());

        // Assert
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> profileService.getProfile());
        assertEquals(ExceptionMessage.getUserNotExistMsg(user.getEmail()), exception.getMessage());
    }

    @Test
    void testPatchProfileAllProps() {
        // Arrange
        User user = mockUser();
        PatchProfileRequest patchProfileRequest = mockPatchProfileRequest("newFirstName", "newLastName");
        mockAuthenticationService(user.getEmail());
        mockUserRepo(user);

        // Act
        ProfileResponse response = profileService.patchProfile(patchProfileRequest);

        // Assert
        assertNotNull(response);
        assertEquals(user.getEmail(), response.getProfile().getEmail());
        assertEquals("newFirstName", response.getProfile().getFirstName());
        assertEquals("newLastName", response.getProfile().getLastName());
    }

    @Test
    void testPatchProfileOneProp() {
        // Arrange
        User user = mockUser();
        PatchProfileRequest patchProfileRequest = mockPatchProfileRequest("newFirstName", null);
        mockAuthenticationService(user.getEmail());
        mockUserRepo(user);

        // Act
        ProfileResponse response = profileService.patchProfile(patchProfileRequest);

        // Assert
        assertNotNull(response);
        assertEquals(user.getEmail(), response.getProfile().getEmail());
        assertEquals("newFirstName", response.getProfile().getFirstName());
        assertEquals(user.getLastname(), response.getProfile().getLastName());
    }

    @Test
    void testPatchProfileNoProps() {
        // Arrange
        User user = mockUser();
        PatchProfileRequest patchProfileRequest = mockPatchProfileRequest(null, null);
        mockAuthenticationService(user.getEmail());
        mockUserRepo(user);

        // Act
        ProfileResponse response = profileService.patchProfile(patchProfileRequest);

        // Assert
        assertNotNull(response);
        assertEquals(user.getEmail(), response.getProfile().getEmail());
        assertEquals(user.getFirstname(), response.getProfile().getFirstName());
        assertEquals(user.getLastname(), response.getProfile().getLastName());
    }

    @Test
    void testPatchProfileSameProps() {
        // Arrange
        User user = mockUser();
        PatchProfileRequest patchProfileRequest = mockPatchProfileRequest(user.getFirstname(), user.getLastname());
        mockAuthenticationService(user.getEmail());
        mockUserRepo(user);

        // Act
        ProfileResponse response = profileService.patchProfile(patchProfileRequest);

        // Assert
        assertNotNull(response);
        assertEquals(user.getEmail(), response.getProfile().getEmail());
        assertEquals(user.getFirstname(), response.getProfile().getFirstName());
        assertEquals(user.getLastname(), response.getProfile().getLastName());
    }

    @Test
    void testPatchProfileWhenNullAuthentication() {
        // Arrange
        when(authenticationService.getAuthentication()).thenReturn(null);

        // Assert
        Exception exception = assertThrows(AuthException.class, () -> profileService.patchProfile(null));
        assertEquals(ExceptionMessage.AUTH_FAILED, exception.getMessage());
    }

    @Test
    void testPatchProfileWhenUserNotFound() {
        // Arrange
        User user = mockUser();
        PatchProfileRequest patchProfileRequest = mockPatchProfileRequest("newFirstName", "newLastName");
        mockAuthenticationService(user.getEmail());

        // Assert
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> profileService.patchProfile(patchProfileRequest));
        assertEquals(ExceptionMessage.getUserNotExistMsg(user.getEmail()), exception.getMessage());
    }
}
