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
        Password password3 = createPassword("Email1", "Password!1234", user);
        Password password4 = createPassword("Email2", "Password!1234", user);
        Password password5 = createPassword("Email3", "Password!1234", user);
        Password password6 = createPassword("Email4", "Password!1234", user);
        Password password7 = createPassword("Email5", "Password!1234", user);
        Password password8 = createPassword("Email6", "Password!1234", user);
        Password password9 = createPassword("Email7", "Password!1234", user);
        Password password10 = createPassword("Email8", "Password!1234", user);
        Password password11 = createPassword("Email9", "Password!1234", user);
        Password password12 = createPassword("Email10", "Password!1234", user);
        Password password13 = createPassword("Email11", "Password!1234", user);
        Password password14 = createPassword("Email12", "Password!1234", user);
        Password password15 = createPassword("Email13", "Password!1234", user);

    }
}
