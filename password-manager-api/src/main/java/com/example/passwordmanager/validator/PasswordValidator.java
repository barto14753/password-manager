package com.example.passwordmanager.validator;

import com.example.passwordmanager.exception.ExceptionMessages;
import com.example.passwordmanager.exception.PasswordException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class PasswordValidator {
    private static final Integer MIN_CHARACTERS = 8;
    private static final Integer MAX_CHARACTERS = 128;
    private static final Integer MIN_LOWERCASE_CHARACTERS  = 1;
    private static final Integer MAX_LOWERCASE_CHARACTERS = MAX_CHARACTERS;
    private static final Integer MIN_UPPERCASE_CHARACTERS = 1;
    private static final Integer MAX_UPPERCASE_CHARACTERS = MAX_CHARACTERS;
    private static final Integer MIN_NUMERIC_CHARACTERS = 1;
    private static final Integer MAX_NUMERIC_CHARACTERS = MAX_CHARACTERS;
    private static final Integer MIN_SPECIAL_CHARACTERS = 1;
    private static final Integer MAX_SPECIAL_CHARACTERS= MAX_CHARACTERS;

    public void validate(String password) throws PasswordException {
        validateCharactersCount(password);
        validateLowerCaseCharactersCount(password);
        validateUpperCaseCharactersCount(password);
        validateNumericCharactersCount(password);
        validateSpecialCharactersCount(password);
    }

    private void validateCount(long actual, int min, int max, PasswordException minEx, PasswordException maxEx) throws PasswordException {
        if (actual < min) {
            throw minEx;
        }
        else if (actual > max) {
            throw maxEx;
        }
    }

    private void validateCharactersCount(String password) throws PasswordException {
        validateCount(
                password.length(),
                MIN_CHARACTERS,
                MAX_CHARACTERS,
                new PasswordException(ExceptionMessages.getPasswordMinCharactersMsg(MIN_CHARACTERS)),
                new PasswordException(ExceptionMessages.getPasswordMaxCharactersMsg(MAX_CHARACTERS)));
    }

    private void validateLowerCaseCharactersCount(String password) throws PasswordException {
        validateCount(
                password.chars().filter(Character::isLowerCase).count(),
                MIN_LOWERCASE_CHARACTERS,
                MAX_LOWERCASE_CHARACTERS,
                new PasswordException(ExceptionMessages.getPasswordMinLowerCaseCharactersMsg(MIN_LOWERCASE_CHARACTERS)),
                new PasswordException(ExceptionMessages.getPasswordMaxLowerCaseCharactersMsg(MAX_LOWERCASE_CHARACTERS)));
    }

    private void validateUpperCaseCharactersCount(String password) throws PasswordException {
        validateCount(
                password.chars().filter(Character::isUpperCase).count(),
                MIN_UPPERCASE_CHARACTERS,
                MAX_UPPERCASE_CHARACTERS,
                new PasswordException(ExceptionMessages.getPasswordMinUpperCaseCharactersMsg(MIN_UPPERCASE_CHARACTERS)),
                new PasswordException(ExceptionMessages.getPasswordMaxUpperCaseCharactersMsg(MAX_UPPERCASE_CHARACTERS)));
    }

    private void validateNumericCharactersCount(String password) throws PasswordException {
        validateCount(
                password.chars().filter(Character::isDigit).count(),
                MIN_NUMERIC_CHARACTERS,
                MAX_NUMERIC_CHARACTERS,
                new PasswordException(ExceptionMessages.getPasswordMinNumericCharactersMsg(MIN_NUMERIC_CHARACTERS)),
                new PasswordException(ExceptionMessages.getPasswordMaxNumericCharactersMsg(MAX_NUMERIC_CHARACTERS)));
    }

    private void validateSpecialCharactersCount(String password) throws PasswordException {
        validateCount(
                password.replaceAll("[A-Za-z0-9\\s]", "").length(),
                MIN_SPECIAL_CHARACTERS,
                MAX_SPECIAL_CHARACTERS,
                new PasswordException(ExceptionMessages.getPasswordMinSpecialCharactersMsg(MIN_SPECIAL_CHARACTERS)),
                new PasswordException(ExceptionMessages.getPasswordMaxSpecialCharactersMsg(MAX_SPECIAL_CHARACTERS)));
    }
}
