package com.example.passwordmanager.service.auth;

import com.example.passwordmanager.config.jwt.JwtConfig;
import com.example.passwordmanager.config.jwt.JwtService;
import com.example.passwordmanager.config.jwt.TokenType;
import com.example.passwordmanager.dto.request.auth.LoginRequest;
import com.example.passwordmanager.dto.request.auth.PasswordResetRequest;
import com.example.passwordmanager.dto.request.auth.RegisterRequest;
import com.example.passwordmanager.dto.response.auth.AuthenticationResponse;
import com.example.passwordmanager.dto.util.BasicUser;
import com.example.passwordmanager.exception.*;
import com.example.passwordmanager.model.Role;
import com.example.passwordmanager.model.User;
import com.example.passwordmanager.repo.user.UserRepo;
import com.example.passwordmanager.validator.PasswordValidator;
import com.example.passwordmanager.validator.RegisterValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TokenService {
    private final UserRepo repository;
    private final RegisterValidator registerValidator;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final PasswordValidator passwordValidator;

    public AuthenticationResponse register(RegisterRequest request) throws RegisterException, PasswordException {
        log.info("User want to register with " + request.getEmail());
        registerValidator.validate(request);
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .isLocked(false)
                .build();
        user = repository.save(user);
        String accessToken = jwtService.generateToken(user, TokenType.ACCESS_TOKEN, jwtConfig.getAccessTokenExp());
        String refreshToken = jwtService.generateToken(user, TokenType.REFRESH_TOKEN, jwtConfig.getRefreshTokenExp());
        log.info("User " + user.getEmail() + " successfully registered");
        return AuthenticationResponse.builder()
                .user(new BasicUser(user))
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse login(LoginRequest request) throws AuthenticationException {
        log.info("User " + request.getEmail() + " try to log in");
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        Authentication auth = authenticationManager.authenticate(token);
        String email = auth.getName();
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new AuthException(ExceptionMessage.getUserNotExistMsg(email)));

        String accessToken = jwtService.generateToken(user, TokenType.ACCESS_TOKEN, jwtConfig.getAccessTokenExp());
        String refreshToken = jwtService.generateToken(user, TokenType.REFRESH_TOKEN, jwtConfig.getRefreshTokenExp());
        log.info("User " + user.getEmail() + " logged in");
        return AuthenticationResponse.builder()
                .user(new BasicUser(user))
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse refresh(HttpServletRequest request) throws AuthException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new AuthException(ExceptionMessage.WRONG_HEADER);
        }
        String refreshToken = authorizationHeader.substring("Bearer ".length());
        String email = jwtService.extractUsername(refreshToken);
        log.info("User " + email + " try to refresh token");

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new AuthException(ExceptionMessage.getUserNotExistMsg(email)));

        if (jwtService.isTokenExpired(refreshToken)) {
            user.setIsLocked(true);
            throw new AuthException(ExceptionMessage.REFRESH_TOKEN_EXPIRED);
        }

        if (!jwtService.isRefreshToken(refreshToken)) {
            user.setIsLocked(true);
            throw new AuthException(ExceptionMessage.WRONG_TOKEN_TYPE_FOR_REFRESH);
        }

        String accessToken = jwtService.generateToken(user, TokenType.ACCESS_TOKEN, jwtConfig.getAccessTokenExp());
        String newRefreshToken = jwtService.generateToken(user, TokenType.REFRESH_TOKEN, jwtConfig.getRefreshTokenExp());
        log.info("User " + email + " successfully refreshed token");
        return AuthenticationResponse.builder()
                .user(new BasicUser(user))
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    public void resetPassword(PasswordResetRequest request) throws AuthException, PasswordException, PasswordResetException {
        String email = request.getEmail();
        String newPassword = request.getNewPassword();
        String oldPassword = request.getOldPassword();

        log.info("User with " + email + " try to reset password");
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new AuthException(ExceptionMessage.getUserNotExistMsg(email)));

        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(email, oldPassword);

        authenticationManager.authenticate(token);

        if (newPassword.equals(oldPassword)) {
            log.info("Password reset for " + email + " failed, because oldPassword = newPassword");
            throw new PasswordResetException("Password reset for " + email + " failed, because oldPassword = newPassword");
        }

        passwordValidator.validate(newPassword);
        String newPasswordEncrypted = passwordEncoder.encode(newPassword);
        user.setPassword(newPasswordEncrypted);
        repository.save(user);

        log.info("Password for " + email + " was reset");
    }
}