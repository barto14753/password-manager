package com.example.passwordmanager.service;

import com.example.passwordmanager.dto.request.PatchProfileRequest;
import com.example.passwordmanager.dto.response.ProfileResponse;
import com.example.passwordmanager.dto.util.BasicUser;
import com.example.passwordmanager.model.User;
import com.example.passwordmanager.repo.user.UserRepo;
import com.example.passwordmanager.service.auth.AuthenticationService;
import com.example.passwordmanager.validator.AuthValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ProfileService {
    private final UserRepo userRepo;
    private final AuthenticationService authService;
    private final AuthValidator authValidator;

    public ProfileResponse getProfile() {
        User user = authValidator.validateUser();

        // Build and return user profile
        log.info("Get profile for  " + user.getEmail());
        return ProfileResponse.builder()
                .profile(new BasicUser(user))
                .build();
    }

    public ProfileResponse patchProfile(PatchProfileRequest patchProfileRequest) {
        User user = authValidator.validateUser();
        log.info("Set profile for " + user.getEmail());

        // If new first name provided update it
        String newFirstName = patchProfileRequest.getFirstName();
        if (newFirstName != null) {
            log.info("Set for " + user.getEmail() + " firstName=" + newFirstName);
            user.setFirstname(newFirstName);
        }

        // If new last name provided update it
        String newLastName = patchProfileRequest.getLastName();
        if (newLastName != null) {
            log.info("Set for " + user.getEmail() + " lastName=" + newLastName);
            user.setLastname(newLastName);
        }

        // Build and return user profile with new data
        return ProfileResponse.builder()
                .profile(new BasicUser(user))
                .build();
    }
}
