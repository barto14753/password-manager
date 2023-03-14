package com.example.passwordmanager.validator;

import com.example.passwordmanager.exception.ExceptionMessage;
import com.example.passwordmanager.exception.RegisterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailValidator {
    private final static String RFC_EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private final static String TOP_LEVEL_DOMAIN_EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private final static String DOTS_EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    private final static String OWASP_EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private final static List<String> regexes = List.of(
            RFC_EMAIL_REGEX, TOP_LEVEL_DOMAIN_EMAIL_REGEX, DOTS_EMAIL_REGEX, OWASP_EMAIL_REGEX);

    public void validate(String email) throws RegisterException {
        if (regexes.stream().anyMatch(regex -> !Pattern.compile(regex).matcher(email).matches())) {
            log.info("Invalid registration with email: " + email);
            throw new RegisterException(ExceptionMessage.getEmailInvalidMsg(email));
        }
    }
}
