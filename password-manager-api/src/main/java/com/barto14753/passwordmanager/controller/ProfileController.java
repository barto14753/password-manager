package com.barto14753.passwordmanager.controller;

import com.barto14753.passwordmanager.dto.request.profile.PatchProfileRequest;
import com.barto14753.passwordmanager.dto.response.ProfileResponse;
import com.barto14753.passwordmanager.service.ProfileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/profile")
@SecurityRequirement(name = "Bearer Authentication")
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping
    ResponseEntity<ProfileResponse> getProfile() {
        return ResponseEntity.ok().body(profileService.getProfile());
    }

    @PatchMapping
    ResponseEntity<ProfileResponse> patchProfile(@RequestBody PatchProfileRequest patchProfileRequest) {
        return ResponseEntity.ok().body(profileService.patchProfile(patchProfileRequest));
    }
}
