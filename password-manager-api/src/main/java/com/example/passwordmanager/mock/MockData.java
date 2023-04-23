package com.example.passwordmanager.mock;

import com.example.passwordmanager.config.ApplicationConfig;
import com.example.passwordmanager.model.Role;
import com.example.passwordmanager.model.User;
import com.example.passwordmanager.repo.user.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Transactional
public class MockData implements CommandLineRunner {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public User createUser(String email, String firstName, String lastName, String password) {
        User user = User.builder()
                .email(email)
                .firstname(firstName)
                .lastname(lastName)
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .isLocked(false)
                .build();
        return userRepo.save(user);
    }
    @Override
    public void run(String... args) throws Exception {
        User user = this.createUser("user@domain.com", "Joe", "Doe", "Password!123");
    }
}
