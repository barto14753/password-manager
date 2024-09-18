package com.barto14753.passwordmanager.validator;

import com.barto14753.passwordmanager.exception.AuthException;
import com.barto14753.passwordmanager.exception.ExceptionMessages;
import com.barto14753.passwordmanager.model.User;
import com.barto14753.passwordmanager.repo.user.UserRepo;
import com.barto14753.passwordmanager.service.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthValidator {
    private final UserRepo userRepo;
    private final AuthenticationService authService;

    public User validateUser() {
        // Retrieve email from auth
        Authentication auth = authService.getAuthentication();
        if (auth == null) {
            throw new AuthException(ExceptionMessages.AUTH_FAILED);
        }
        String email = auth.getName();

        // Find user
        Optional<User> user = userRepo.findByEmail(email);

        // If user not found throw exception
        if (user.isEmpty()) {
            String errorMsg = ExceptionMessages.getUserNotExistMsg(email);
            log.info(errorMsg);
            throw new UsernameNotFoundException(errorMsg);
        }
        return user.get();
    }
}
