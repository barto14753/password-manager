package com.example.passwordmanager.service;

import com.example.passwordmanager.config.jwt.JwtConfig;
import com.example.passwordmanager.config.jwt.JwtService;
import com.example.passwordmanager.config.jwt.TokenType;
import com.example.passwordmanager.dto.request.LoginRequest;
import com.example.passwordmanager.dto.request.PasswordResetRequest;
import com.example.passwordmanager.dto.request.RegisterRequest;
import com.example.passwordmanager.dto.response.AuthenticationResponse;
import com.example.passwordmanager.exception.*;
import com.example.passwordmanager.model.Role;
import com.example.passwordmanager.model.User;
import com.example.passwordmanager.repo.user.UserRepo;
import com.example.passwordmanager.validator.PasswordValidator;
import com.example.passwordmanager.validator.RegisterValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {
    private final UserRepo repository;
    private final RegisterValidator registerValidator;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final PasswordValidator passwordValidator;

    public AuthenticationResponse register(RegisterRequest request) throws RegisterException, PasswordException {
        registerValidator.validate(request);
        User user = User.builder()
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
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
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse login(LoginRequest request) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
                
        authenticationManager.authenticate(token);
        String email = request.getEmail();
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException(ExceptionMessage.getUserNotExistMsg(email)));

        String accessToken = jwtService.generateToken(user, TokenType.ACCESS_TOKEN, jwtConfig.getAccessTokenExp());
        String refreshToken = jwtService.generateToken(user, TokenType.REFRESH_TOKEN, jwtConfig.getRefreshTokenExp());
        log.info("User " + user.getEmail() + " logged in");
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse refresh(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new AuthenticationException(ExceptionMessage.WRONG_HEADER);
        }
        String refreshToken = authorizationHeader.substring("Bearer ".length());;
        String email = jwtService.extractUsername(refreshToken);

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException(ExceptionMessage.getUserNotExistMsg(email)));

        if (jwtService.isTokenExpired(refreshToken)) {
            user.setIsLocked(true);
            throw new AuthenticationException(ExceptionMessage.REFRESH_TOKEN_EXPIRED);
        }

        if (!jwtService.isRefreshToken(refreshToken)) {
            user.setIsLocked(true);
            throw new AuthenticationException(ExceptionMessage.WRONG_TOKEN_TYPE_FOR_REFRESH);
        }

        String accessToken = jwtService.generateToken(user, TokenType.ACCESS_TOKEN, jwtConfig.getAccessTokenExp());
        String newRefreshToken = jwtService.generateToken(user, TokenType.REFRESH_TOKEN, jwtConfig.getRefreshTokenExp());
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    public void resetPassword(PasswordResetRequest request) throws AuthenticationException, PasswordException, PasswordResetException {
        String email = request.getEmail();
        String newPassword = request.getNewPassword();
        String oldPassword = request.getOldPassword();

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException(ExceptionMessage.getUserNotExistMsg(email)));

        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(email, oldPassword);

        authenticationManager.authenticate(token);

        if (newPassword.equals(oldPassword)) {
            log.info("Password reset for " + email + " failed, because oldPassword = newPassword");
            throw new PasswordResetException("Password reset for " + email + " failed, because oldPassword = newPassword");
        }
        passwordValidator.validate(newPassword);
        user.setPassword(newPassword);
        log.info("Password for " + email + " reset");
    }
}