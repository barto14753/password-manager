package com.example.passwordmanager.validator;

import com.example.passwordmanager.exception.ExceptionMessage;
import com.example.passwordmanager.exception.RegisterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {
    @InjectMocks
    private EmailValidator emailValidator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "email@domain.com",
            "Email@Domain.com",
            "email123@domain.com",
            "Email123@domain.com",
            "Email123@Domain123.com",
            "a@b.pl",
            "a-b@d.pl",
            "abc@d-e.pl"
    })
    void testEmailValidateSuccessful(String email) {
        // Assert
        assertDoesNotThrow(() -> emailValidator.validate(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "@",
            "a@b",
            "a@b.c",
            "abc",
            "@.",
            "abc!@d.pl"
    })
    void testEmailValidateFailure(String wrongEmail) {
        // Assert
        Exception exception = assertThrows(RegisterException.class, () -> emailValidator.validate(wrongEmail));
        assertEquals(ExceptionMessage.getEmailInvalidMsg(wrongEmail), exception.getMessage());
    }
}
