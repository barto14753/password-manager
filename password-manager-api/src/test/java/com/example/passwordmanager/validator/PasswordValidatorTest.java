package com.example.passwordmanager.validator;

import com.example.passwordmanager.exception.PasswordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {
    @InjectMocks
    private PasswordValidator passwordValidator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Password!123",
            "passworD-1",
            "QuiteLongPasswordWithZeroInTheEnd?0",
            "!!!aA0aA???",
            "Abcd123%"
    })
    void testPasswordValidateSuccessful(String password) {
        // Assert
        assertDoesNotThrow(() -> passwordValidator.validate(password));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "Short!1",
            "123!TooLonggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg" +
                    "ggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg",
            "nouppercase!123",
            "NOLOWWERCASE!123",
            "NoSpecialCharacter123",
            "NoNumbers!",
            "12345678!"
    })
    void testPasswordValidateFailure(String wrongPassword) {
        // Assert
        assertThrows(PasswordException.class, () -> passwordValidator.validate(wrongPassword));
    }
}
