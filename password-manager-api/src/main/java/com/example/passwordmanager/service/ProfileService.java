package com.example.passwordmanager.service;

import com.example.passwordmanager.dto.request.PatchProfileRequest;
import com.example.passwordmanager.dto.response.ProfileResponse;
import com.example.passwordmanager.dto.util.BasicUser;
import com.example.passwordmanager.exception.AuthException;
import com.example.passwordmanager.exception.ExceptionMessage;
import com.example.passwordmanager.model.User;
import com.example.passwordmanager.repo.user.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ProfileService {
    private final UserRepo userRepo;
    private final AuthenticationService authService;

    public ProfileResponse getProfile() {
        // Retrieve email from auth
        Authentication auth = authService.getAuthentication();
        if (auth == null) {
            throw new AuthException(ExceptionMessage.AUTH_FAILED);
        }
        String email = auth.getName();

        // Find user
        Optional<User> user = userRepo.findByEmail(email);

        // If user not found throw exception
        if (user.isEmpty()) {
            String errorMsg = ExceptionMessage.getUserNotExistMsg(email);
            log.info(errorMsg);
            throw new UsernameNotFoundException(errorMsg);
        }

        // Build and return user profile
        log.info("Get profile for  " + email);
        return ProfileResponse.builder()
                .profile(new BasicUser(user.get()))
                .build();
    }

    public ProfileResponse patchProfile(PatchProfileRequest patchProfileRequest) {
        // Retrieve email from auth
        Authentication auth = authService.getAuthentication();
        if (auth == null) {
            throw new AuthException(ExceptionMessage.AUTH_FAILED);
        }
        String email = auth.getName();

        // Find user
        Optional<User> optionalUser = userRepo.findByEmail(email);

        // If user not found throw exception
        if (optionalUser.isEmpty()) {
            String errorMsg = ExceptionMessage.getUserNotExistMsg(email);
            log.info(errorMsg);
            throw new UsernameNotFoundException(errorMsg);
        }

        User user = optionalUser.get();
        log.info("Set profile for " + email);

        // If new first name provided update it
        String newFirstName = patchProfileRequest.getFirstName();
        if (newFirstName != null) {
            log.info("Set for " + email + " firstName=" + newFirstName);
            user.setFirstname(newFirstName);
        }

        // If new last name provided update it
        String newLastName = patchProfileRequest.getLastName();
        if (newLastName != null) {
            log.info("Set for " + email + " lastName=" + newLastName);
            user.setLastname(newLastName);
        }

        // Build and return user profile with new data
        return ProfileResponse.builder()
                .profile(new BasicUser(user))
                .build();
    }
}
