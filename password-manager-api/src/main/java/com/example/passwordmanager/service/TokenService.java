package com.example.passwordmanager.service;

import com.example.passwordmanager.config.jwt.JwtService;
import com.example.passwordmanager.config.jwt.TokenType;
import com.example.passwordmanager.dto.request.LoginRequest;
import com.example.passwordmanager.dto.request.RegisterRequest;
import com.example.passwordmanager.dto.response.AuthenticationResponse;
import com.example.passwordmanager.exception.AuthenticationException;
import com.example.passwordmanager.exception.ExceptionMessage;
import com.example.passwordmanager.exception.RegisterException;
import com.example.passwordmanager.model.Role;
import com.example.passwordmanager.model.User;
import com.example.passwordmanager.repo.user.UserRepo;
import com.example.passwordmanager.validator.RegisterValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final UserRepo repository;
    private final RegisterValidator registerValidator;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final long ACCESS_TOKEN_EXPIRATION_TIME = 2 * 60 * 60 * 1000;
    private final long REFRESH_TOKEN_EXPIRATION_TIME = 5 * 60 * 60 * 1000;

    public AuthenticationResponse register(RegisterRequest request) throws RegisterException {
        registerValidator.validate(request);
        User user = User.builder()
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        String accessToken = jwtService.generateToken(user, TokenType.ACCESS_TOKEN, ACCESS_TOKEN_EXPIRATION_TIME);
        String refreshToken = jwtService.generateToken(user, TokenType.REFRESH_TOKEN, REFRESH_TOKEN_EXPIRATION_TIME);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse login(LoginRequest request) throws AuthenticationException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        String email = request.getEmail();
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException(ExceptionMessage.getUserNotExistMsg(email)));

        String accessToken = jwtService.generateToken(user, TokenType.ACCESS_TOKEN, ACCESS_TOKEN_EXPIRATION_TIME);
        String refreshToken = jwtService.generateToken(user, TokenType.REFRESH_TOKEN, REFRESH_TOKEN_EXPIRATION_TIME);
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

        String accessToken = jwtService.generateToken(user, TokenType.ACCESS_TOKEN, ACCESS_TOKEN_EXPIRATION_TIME);
        String newRefreshToken = jwtService.generateToken(user, TokenType.REFRESH_TOKEN, REFRESH_TOKEN_EXPIRATION_TIME);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

}