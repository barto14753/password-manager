package com.example.passwordmanager.validator;

import com.example.passwordmanager.exception.ExceptionMessage;
import com.example.passwordmanager.exception.RegisterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class PasswordValidator {
    private static final Integer MIN_CHARACTERS = 8;
    private static final Integer MAX_CHARACTERS = 20;
    private static final Integer MIN_LOWERCASE_CHARACTERS  = 1;
    private static final Integer MAX_LOWERCASE_CHARACTERS = MAX_CHARACTERS;
    private static final Integer MIN_UPPERCASE_CHARACTERS = 1;
    private static final Integer MAX_UPPERCASE_CHARACTERS = MAX_CHARACTERS;
    private static final Integer MIN_NUMERIC_CHARACTERS = 1;
    private static final Integer MAX_NUMERIC_CHARACTERS = MAX_CHARACTERS;
    private static final Integer MIN_SPECIAL_CHARACTERS = 1;
    private static final Integer MAX_SPECIAL_CHARACTERS= MAX_CHARACTERS;

    public void validate(String password) throws RegisterException {
        validateCharactersCount(password);
        validateLowerCaseCharactersCount(password);
        validateUpperCaseCharactersCount(password);
        validateNumericCharactersCount(password);
        validateSpecialCharactersCount(password);
    }

    private void validateCount(long actual, int min, int max, RegisterException minEx, RegisterException maxEx) throws RegisterException {
        if (actual < min) {
            throw minEx;
        }
        else if (actual > max) {
            throw maxEx;
        }
    }

    private void validateCharactersCount(String password) throws RegisterException {
        validateCount(
                password.length(),
                MIN_CHARACTERS,
                MAX_CHARACTERS,
                new RegisterException(ExceptionMessage.getPasswordMinCharactersMsg(MIN_CHARACTERS)),
                new RegisterException(ExceptionMessage.getPasswordMaxCharactersMsg(MAX_CHARACTERS)));
    }

    private void validateLowerCaseCharactersCount(String password) throws RegisterException {
        validateCount(
                password.chars().filter(Character::isLowerCase).count(),
                MIN_LOWERCASE_CHARACTERS,
                MAX_LOWERCASE_CHARACTERS,
                new RegisterException(ExceptionMessage.getPasswordMinLowerCaseCharactersMsg(MIN_LOWERCASE_CHARACTERS)),
                new RegisterException(ExceptionMessage.getPasswordMaxLowerCaseCharactersMsg(MAX_LOWERCASE_CHARACTERS)));
    }

    private void validateUpperCaseCharactersCount(String password) throws RegisterException {
        validateCount(
                password.chars().filter(Character::isUpperCase).count(),
                MIN_UPPERCASE_CHARACTERS,
                MAX_UPPERCASE_CHARACTERS,
                new RegisterException(ExceptionMessage.getPasswordMinUpperCaseCharactersMsg(MIN_UPPERCASE_CHARACTERS)),
                new RegisterException(ExceptionMessage.getPasswordMaxUpperCaseCharactersMsg(MAX_UPPERCASE_CHARACTERS)));
    }

    private void validateNumericCharactersCount(String password) throws RegisterException {
        validateCount(
                password.chars().filter(Character::isDigit).count(),
                MIN_NUMERIC_CHARACTERS,
                MAX_NUMERIC_CHARACTERS,
                new RegisterException(ExceptionMessage.getPasswordMinNumericCharactersMsg(MIN_NUMERIC_CHARACTERS)),
                new RegisterException(ExceptionMessage.getPasswordMaxNumericCharactersMsg(MAX_NUMERIC_CHARACTERS)));
    }

    private void validateSpecialCharactersCount(String password) throws RegisterException {
        validateCount(
                password.replaceAll("[A-Za-z0-9\\s]", "").length(),
                MIN_SPECIAL_CHARACTERS,
                MAX_SPECIAL_CHARACTERS,
                new RegisterException(ExceptionMessage.getPasswordMinSpecialCharactersMsg(MIN_SPECIAL_CHARACTERS)),
                new RegisterException(ExceptionMessage.getPasswordMaxSpecialCharactersMsg(MAX_SPECIAL_CHARACTERS)));
    }
}
