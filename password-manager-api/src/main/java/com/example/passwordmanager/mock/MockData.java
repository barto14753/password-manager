package com.example.passwordmanager.mock;

import com.example.passwordmanager.model.Password;
import com.example.passwordmanager.model.Role;
import com.example.passwordmanager.model.User;
import com.example.passwordmanager.repo.user.PasswordRepo;
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
    private final PasswordRepo passwordRepo;
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
        userRepo.save(user);
        return user;
    }

    public Password createPassword(String name, String value, User owner) {
        Password password = Password.builder()
                .name(name)
                .value(passwordEncoder.encode(value))
                .owner(owner)
                .created(System.currentTimeMillis())
                .modified(System.currentTimeMillis())
                .build();
        passwordRepo.save(password);
        return password;
    }
    @Override
    public void run(String... args) throws Exception {
        User user = this.createUser("user@domain.com", "Joe", "Doe", "Password!123");
        User user1 = this.createUser("user1@domain.com", "Joe", "Doe", "Password!123");
        User user2 = this.createUser("user2@domain.com", "Joe", "Doe", "Password!123");

        Password password1 = createPassword("Google", "Password!123", user);
        Password password2 = createPassword("Email", "Password!1234", user);
    }
}
