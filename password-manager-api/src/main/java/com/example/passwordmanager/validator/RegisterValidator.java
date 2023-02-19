package com.example.passwordmanager.validator;

import com.example.passwordmanager.dto.request.RegisterRequest;
import com.example.passwordmanager.exception.ExceptionMessage;
import com.example.passwordmanager.exception.RegisterException;
import com.example.passwordmanager.repo.user.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@Transactional
@RequiredArgsConstructor
public class RegisterValidator {
    private final UserRepo userRepo;
    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;

    public void validate(RegisterRequest registerRequest) throws RegisterException {
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();

        validateEmailRegex(email);
        validateEmailIsUnique(email);
        validatePassword(password);
    }

    private void validatePassword(String password) throws RegisterException {
        passwordValidator.validate(password);
    }

    private void validateEmailRegex(String email) throws RegisterException {
        emailValidator.validate(email);
    }

    private void validateEmailIsUnique(String email) throws RegisterException {
        if (userRepo.findByEmail(email).isPresent()) {
            throw new RegisterException(ExceptionMessage.getEmailTakenMsg(email));
        }
    }
}
