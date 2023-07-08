package com.example.passwordmanager.service.password;

import com.example.passwordmanager.dto.response.password.GetAllPasswordsResponse;
import com.example.passwordmanager.dto.response.password.GetPasswordResponse;
import com.example.passwordmanager.dto.util.BasicPassword;
import com.example.passwordmanager.exception.ExceptionMessage;
import com.example.passwordmanager.exception.PasswordException;
import com.example.passwordmanager.model.Password;
import com.example.passwordmanager.model.User;
import com.example.passwordmanager.repo.user.PasswordRepo;
import com.example.passwordmanager.repo.user.UserRepo;
import com.example.passwordmanager.service.auth.AuthenticationService;
import com.example.passwordmanager.validator.AuthValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PasswordService {
    private final UserRepo userRepo;
    private final PasswordRepo passwordRepo;
    private final AuthenticationService authService;
    private final AuthValidator authValidator;

    public GetPasswordResponse getPasswordResponse(Long id) throws PasswordException {
        User user = authValidator.validateUser();
        Password password = getPasswordOwnedBy(user, id);
        return GetPasswordResponse.builder()
                .password(new BasicPassword(password))
                .build();
    }

    public GetAllPasswordsResponse getAllPasswordsResponse() {
        User user = authValidator.validateUser();
        List<Password> passwords = passwordRepo.findAllByOwner(user);
        List<BasicPassword> basicPasswords = passwords.stream().map(BasicPassword::new).toList();
        return GetAllPasswordsResponse.builder()
                .passwords(basicPasswords)
                .build();
    }

    private Password getPassword(Long id) throws PasswordException {
        Optional<Password> password = passwordRepo.findById(id);
        if (password.isEmpty()) {
            String errorMessage = ExceptionMessage.getPasswordWithIdNotFound(id);
            log.info(errorMessage);
            throw new PasswordException(errorMessage);
        }

        return password.get();
    }

    private Password getPasswordOwnedBy(User owner, Long id) throws PasswordException {
        Password password = getPassword(id);
        if (!password.getOwner().equals(owner)) {
            String errorMessage = ExceptionMessage.getPasswordWithIdOwnedByNotFound(owner.getEmail(), id);
            log.info(errorMessage);
            throw new PasswordException(errorMessage);
        }
        return password;
    }
}
