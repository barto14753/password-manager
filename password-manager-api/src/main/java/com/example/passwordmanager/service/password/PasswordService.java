package com.example.passwordmanager.service.password;

import com.example.passwordmanager.crypto.Encrypter;
import com.example.passwordmanager.dto.request.password.CreatePasswordRequest;
import com.example.passwordmanager.dto.response.password.CreatePasswordResponse;
import com.example.passwordmanager.dto.response.password.GetAllPasswordsResponse;
import com.example.passwordmanager.dto.response.password.GetPasswordResponse;
import com.example.passwordmanager.dto.util.BasicPassword;
import com.example.passwordmanager.exception.ExceptionMessages;
import com.example.passwordmanager.exception.password.PasswordCreationException;
import com.example.passwordmanager.exception.password.PasswordException;
import com.example.passwordmanager.exception.password.PasswordOwnershipException;
import com.example.passwordmanager.exception.util.ExceptionMessage;
import com.example.passwordmanager.model.Password;
import com.example.passwordmanager.model.User;
import com.example.passwordmanager.repo.user.PasswordRepo;
import com.example.passwordmanager.validator.AuthValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PasswordService {
    private final PasswordRepo passwordRepo;
    private final AuthValidator authValidator;
    private final Encrypter encrypter;

    public GetPasswordResponse getPasswordResponse(Long id) throws PasswordException, GeneralSecurityException {
        User user = authValidator.validateUser();
        Password password = getPasswordOwnedBy(user, id);
        String decryptedPassword = encrypter.decrypt(password.getEncryptedValue());
        log.info("User " + user.getEmail() + " get password with id " + id);
        return GetPasswordResponse.builder()
                .password(new BasicPassword(password, decryptedPassword))
                .build();
    }

    public GetAllPasswordsResponse getAllPasswordsResponse() {
        User user = authValidator.validateUser();
        List<Password> passwords = passwordRepo.findAllByOwner(user);
        List<BasicPassword> basicPasswords = passwords.stream().map(password -> {
            try {
                return new BasicPassword(password, encrypter.decrypt(password.getEncryptedValue()));
            } catch (GeneralSecurityException e) {
                return null;
            }
        }).toList();
        log.info("User " + user.getEmail() + " get all passwords with id");
        return GetAllPasswordsResponse.builder()
                .passwords(basicPasswords)
                .build();
    }

    public CreatePasswordResponse createPasswordResponse(CreatePasswordRequest request) throws PasswordCreationException, GeneralSecurityException {
        User user = authValidator.validateUser();
        Password password = createPassword(user, request.getName(), request.getValue());
        String decryptedPassword = encrypter.decrypt(password.getEncryptedValue());
        log.info("User " + user.getEmail() + " create password + " + password.getName() + " with id");
        return CreatePasswordResponse.builder()
                .password(new BasicPassword(password, decryptedPassword))
                .build();
    }

    public void deletePassword(Long id) throws PasswordException {
        User user = authValidator.validateUser();
        Password password = getPasswordOwnedBy(user, id);
        passwordRepo.delete(password);
        log.info("User " + user.getEmail() + " deleted password + " + password.getName() + " with id");
    }

    private Password getPassword(Long id) throws PasswordException {
        Optional<Password> password = passwordRepo.findById(id);
        if (password.isEmpty()) {
            String errorMessage = ExceptionMessages.getPasswordWithIdNotFound(id);
            log.info(errorMessage);
            throw new PasswordException(errorMessage);
        }

        return password.get();
    }

    private Password getPasswordOwnedBy(User owner, Long id) throws PasswordException {
        Password password = getPassword(id);
        if (!password.getOwner().equals(owner)) {
            ExceptionMessage msg = ExceptionMessages.getPasswordWithIdOwnedByNotFound(owner.getEmail(), id);
            log.info(msg.getLogMessage());
            throw new PasswordOwnershipException(msg.getMessage());
        }
        return password;
    }

    private Password createPassword(User owner, String name, String value) throws PasswordCreationException, GeneralSecurityException {
        if (name == null || name.isEmpty()) {
            ExceptionMessage msg = ExceptionMessages.getPasswordNameCannotBeNull(owner.getEmail());
            log.info(msg.getLogMessage());
            throw new PasswordCreationException(msg.getMessage());
        }
        Long creationTime = System.currentTimeMillis();
        Password password = Password.builder()
                .name(name)
                .encryptedValue(encrypter.encrypt(value))
                .owner(owner)
                .created(creationTime)
                .modified(creationTime)
                .build();
        passwordRepo.save(password);
        return password;
    }
}
