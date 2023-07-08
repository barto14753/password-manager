package com.example.passwordmanager.service.auth;

import com.example.passwordmanager.config.jwt.JwtConfig;
import com.example.passwordmanager.config.jwt.JwtService;
import com.example.passwordmanager.config.jwt.TokenType;
import com.example.passwordmanager.dto.request.auth.LoginRequest;
import com.example.passwordmanager.dto.request.auth.PasswordResetRequest;
import com.example.passwordmanager.dto.request.auth.RegisterRequest;
import com.example.passwordmanager.dto.response.auth.AuthenticationResponse;
import com.example.passwordmanager.exception.*;
import com.example.passwordmanager.model.Role;
import com.example.passwordmanager.model.User;
import com.example.passwordmanager.repo.user.UserRepo;
import com.example.passwordmanager.validator.PasswordValidator;
import com.example.passwordmanager.validator.RegisterValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

class TokenServiceTest {

    @Mock
    private UserRepo repository;

    @Mock
    private RegisterValidator registerValidator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtConfig jwtConfig;

    @Mock
    private PasswordValidator passwordValidator;

    @Mock
    private Authentication authentication;

    @Mock
    private HttpServletRequest httpRequest;

    @InjectMocks
    private TokenService tokenService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private RegisterRequest mockRegisterRequest() {
        return RegisterRequest.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("Password!123")
                .build();
    }

    private LoginRequest mockLoginRequest() {
        return LoginRequest.builder()
                .email("john.doe@example.com")
                .password("Password!123")
                .build();
    }

    private PasswordResetRequest mockPasswordResetRequest(String email, String oldPassword, String newPassword) {
        return PasswordResetRequest.builder()
                .email(email)
                .oldPassword(oldPassword)
                .newPassword(newPassword)
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

    private void mockRegisterValidator() throws PasswordException, RegisterException {
        Mockito.doNothing().when(registerValidator).validate(Mockito.any(RegisterRequest.class));
    }

    private void mockPasswordEncoder(String encodedPassword) {
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
    }

    private void mockAuthenticationManager(String email) {
        when(authentication.getName()).thenReturn(email);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
    }

    private void mockPasswordValidator(String password, boolean success) throws PasswordException {
        if (success) {
            Mockito.doNothing().when(passwordValidator).validate(password);
        }
        else {
            Mockito.doThrow(PasswordException.class).when(passwordValidator).validate(password);
        }
    }

    private void mockUserRepo(User user) {
        when(repository.save(user)).thenReturn(user);
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    }

    private void mockJwtTokens(String accessToken, String refreshToken) {
        when(jwtService.generateToken(any(User.class), eq(TokenType.ACCESS_TOKEN), anyLong())).thenReturn(accessToken);
        when(jwtService.generateToken(any(User.class), eq(TokenType.REFRESH_TOKEN), anyLong())).thenReturn(refreshToken);
        when(jwtConfig.getAccessTokenExp()).thenReturn(3600L);
        when(jwtConfig.getRefreshTokenExp()).thenReturn(604800L);
    }

    private void mockHttpRequestWithJWT(String jwt) {
        when(httpRequest.getHeader(AUTHORIZATION)).thenReturn(jwt);
    }

    private void mockExtractUsernameFromJWT(String jwt, String username) {
        when(jwtService.extractUsername(jwt)).thenReturn(username);
    }

    private void mockIsExpiredJWT(String jwt, boolean isExpired) {
        when(jwtService.isTokenExpired(jwt)).thenReturn(isExpired);
    }

    private void mockIsRefreshToken(String jwt, boolean isRefreshToken) {
        when(jwtService.isRefreshToken(jwt)).thenReturn(isRefreshToken);
    }

    @Test
    void testRegisterUser() throws RegisterException, PasswordException {
        // Arrange
        RegisterRequest request = mockRegisterRequest();
        User registeredUser = mockUser();
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        mockRegisterValidator();
        mockPasswordEncoder(registeredUser.getPassword());
        mockUserRepo(registeredUser);
        mockJwtTokens(accessToken, refreshToken);

        // Act
        AuthenticationResponse response = tokenService.register(request);

        // Assert
        assertNotNull(response);
        assertEquals(registeredUser.getEmail(), response.getUser().getEmail());
        assertEquals(registeredUser.getFirstname(), response.getUser().getFirstName());
        assertEquals(registeredUser.getLastname(), response.getUser().getLastName());
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
    }

    @Test
    void testRegisterUserWhenValidationFail() throws RegisterException, PasswordException {
        // Arrange
        RegisterRequest request = mockRegisterRequest();
        Mockito.doThrow(RegisterException.class).when(registerValidator).validate(Mockito.any(RegisterRequest.class));

        // Assert
        assertThrows(RegisterException.class, () -> tokenService.register(request));
    }

    @Test
    void testRegisterUserWhenPasswordValidationFail() throws RegisterException, PasswordException {
        // Arrange
        RegisterRequest request = mockRegisterRequest();
        Mockito.doThrow(PasswordException.class).when(registerValidator).validate(Mockito.any(RegisterRequest.class));

        // Assert
        assertThrows(PasswordException.class, () -> tokenService.register(request));
    }

    @Test
    void testLoginUser() throws AuthException {
        // Arrange
        User user = mockUser();
        LoginRequest loginRequest = mockLoginRequest();
        mockAuthenticationManager(loginRequest.getEmail());
        mockUserRepo(user);
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        mockJwtTokens(accessToken, refreshToken);

        // Act
        AuthenticationResponse response = tokenService.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(user.getEmail(), response.getUser().getEmail());
        assertEquals(user.getFirstname(), response.getUser().getFirstName());
        assertEquals(user.getLastname(), response.getUser().getLastName());
        assertEquals(accessToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
    }

    @Test
    void testLoginUserWhenAuthenticationFail() {
        // Arrange
        LoginRequest loginRequest = mockLoginRequest();
        Mockito.doThrow(AuthException.class).when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Assert
        assertThrows(AuthException.class, () -> tokenService.login(loginRequest));
    }

    @Test
    void testLoginUserWhenUserNotFound() {
        // Arrange
        LoginRequest loginRequest = mockLoginRequest();
        mockAuthenticationManager(loginRequest.getEmail());


        // Assert
        Exception exception = assertThrows(AuthException.class, () -> tokenService.login(loginRequest));
        assertEquals(ExceptionMessages.getUserNotExistMsg(loginRequest.getEmail()), exception.getMessage());
    }
    @Test
    void testRefreshToken() {
        // Arrange
        User user = mockUser();
        String correctJwt = "CorrectRefreshToken";
        String correctHeader = "Bearer " + correctJwt;
        mockHttpRequestWithJWT(correctHeader);
        mockExtractUsernameFromJWT(correctJwt, user.getEmail());
        mockIsExpiredJWT(correctJwt, false);
        mockIsRefreshToken(correctJwt, true);
        mockUserRepo(user);

        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        mockJwtTokens(accessToken, refreshToken);

        // Act
        AuthenticationResponse response = tokenService.refresh(httpRequest);

        // Assert
        assertNotNull(response);
        assertEquals(user.getEmail(), response.getUser().getEmail());
        assertEquals(user.getFirstname(), response.getUser().getFirstName());
        assertEquals(user.getLastname(), response.getUser().getLastName());
        assertEquals(accessToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "Not Bearer"})
    @NullSource
    void testRefreshTokenWhenWrongHeader(String wrongHeader) {
        // Arrange
        mockHttpRequestWithJWT(wrongHeader);

        // Assert
        Exception exception = assertThrows(AuthException.class, () -> tokenService.refresh(httpRequest));
        assertEquals(ExceptionMessages.WRONG_HEADER, exception.getMessage());
    }

    @Test
    void testRefreshTokenWhenUserNotFound() {
        // Arrange
        User user = mockUser();
        String correctJwt = "token";
        String correctHeader = "Bearer " + correctJwt;
        mockHttpRequestWithJWT(correctHeader);
        mockExtractUsernameFromJWT(correctJwt, user.getEmail());
        mockIsExpiredJWT(correctJwt, false);

        // Assert
        Exception exception = assertThrows(AuthException.class, () -> tokenService.refresh(httpRequest));
        assertEquals(ExceptionMessages.getUserNotExistMsg(user.getEmail()), exception.getMessage());
    }

    @Test
    void testRefreshTokenWhenTokenIsExpired() {
        // Arrange
        User user = mockUser();
        String correctJwt = "token";
        String correctHeader = "Bearer " + correctJwt;
        mockHttpRequestWithJWT(correctHeader);
        mockExtractUsernameFromJWT(correctJwt, user.getEmail());
        mockUserRepo(user);
        mockIsExpiredJWT(correctJwt, true);

        // Assert
        Exception exception = assertThrows(AuthException.class, () -> tokenService.refresh(httpRequest));
        assertEquals(ExceptionMessages.REFRESH_TOKEN_EXPIRED, exception.getMessage());
    }

    @Test
    void testRefreshTokenWhenTokenIsNotRefreshToken() {
        // Arrange
        User user = mockUser();
        String correctJwt = "token";
        String correctHeader = "Bearer " + correctJwt;
        mockHttpRequestWithJWT(correctHeader);
        mockExtractUsernameFromJWT(correctJwt, user.getEmail());
        mockUserRepo(user);
        mockIsExpiredJWT(correctJwt, false);
        mockIsRefreshToken(correctJwt, false);

        // Assert
        Exception exception = assertThrows(AuthException.class, () -> tokenService.refresh(httpRequest));
        assertEquals(ExceptionMessages.WRONG_TOKEN_TYPE_FOR_REFRESH, exception.getMessage());
    }

    @Test
    void testPasswordReset() throws PasswordException, PasswordResetException {
        // Arrange
        User user = mockUser();
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        String newPasswordEncoded = passwordEncoder.encode(newPassword);
        PasswordResetRequest passwordResetRequest = mockPasswordResetRequest(user.getEmail(), oldPassword, newPassword);
        mockUserRepo(user);
        mockAuthenticationManager(user.getEmail());
        mockPasswordValidator(newPassword, true);

        // Act
        tokenService.resetPassword(passwordResetRequest);

        // Assert
        assertEquals(newPasswordEncoded, user.getPassword());
    }

    @Test
    void testPasswordResetWhenUserNotFound() {
        // Arrange
        User user = mockUser();
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        PasswordResetRequest passwordResetRequest = mockPasswordResetRequest(user.getEmail(), oldPassword, newPassword);

        // Assert
        Exception exception = assertThrows(AuthException.class, () -> tokenService.resetPassword(passwordResetRequest));
        assertEquals(ExceptionMessages.getUserNotExistMsg(user.getEmail()), exception.getMessage());
    }

    @Test
    void testPasswordResetWhenUserNotAuthenticated() {
        // Arrange
        User user = mockUser();
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        PasswordResetRequest passwordResetRequest = mockPasswordResetRequest(user.getEmail(), oldPassword, newPassword);
        mockUserRepo(user);
        Mockito.doThrow(AuthException.class).when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));


        // Assert
        assertThrows(AuthException.class, () -> tokenService.resetPassword(passwordResetRequest));
    }

    @Test
    void testPasswordResetWhenNewPasswordIsTheSame() {
        // Arrange
        User user = mockUser();
        String oldPassword = "oldPassword";
        String newPassword = "oldPassword";
        PasswordResetRequest passwordResetRequest = mockPasswordResetRequest(user.getEmail(), oldPassword, newPassword);
        mockUserRepo(user);
        mockAuthenticationManager(user.getEmail());

        // Assert
        Exception exception = assertThrows(PasswordResetException.class, () -> tokenService.resetPassword(passwordResetRequest));
        assertEquals("Password reset for " + user.getEmail() + " failed, because oldPassword = newPassword", exception.getMessage());

    }

    @Test
    void testPasswordResetWhenNewPasswordIsNotValid() throws PasswordException {
        // Arrange
        User user = mockUser();
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        PasswordResetRequest passwordResetRequest = mockPasswordResetRequest(user.getEmail(), oldPassword, newPassword);
        mockUserRepo(user);
        mockAuthenticationManager(user.getEmail());
        mockPasswordValidator(newPassword, false);

        // Assert
        assertThrows(PasswordException.class, () -> tokenService.resetPassword(passwordResetRequest));

    }
}
