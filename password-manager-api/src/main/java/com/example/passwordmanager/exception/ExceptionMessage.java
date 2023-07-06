package com.example.passwordmanager.exception;

public class ExceptionMessage {
    private ExceptionMessage() {}

    // AUTHENTICATION EXCEPTIONS
    public static final String AUTH_FAILED = "Authentication failed";

    public static String getUserNotExistMsg(String email) {
        return "User with email " + email + " not exist";
    }

    public static final String REFRESH_TOKEN_EXPIRED = "Refresh token has expired, account will be locked";
    public static final String WRONG_TOKEN_TYPE_FOR_REFRESH = "You have to use refresh token for this action";
    public static final String WRONG_HEADER = "You need to provide token inside header";

    public static final String  MIN_CHARACTERS_MSG = "Password needs to have minimum ";
    public static final String  MAX_CHARACTERS_MSG = "Password needs to have maximum ";

    // REGISTER EXCEPTIONS
    public static String getEmailTakenMsg(String email) {
        return "User with email " + email + " already exist";
    }

    public static String getEmailInvalidMsg(String email) {
        return "Email " + email + " is not valid";
    }

    public static String getPasswordMinCharactersMsg(int count) {
        return MIN_CHARACTERS_MSG + count + " characters";
    }

    public static String getPasswordMaxCharactersMsg(int count) {
        return MAX_CHARACTERS_MSG + count + " characters";
    }

    public static String getPasswordMinLowerCaseCharactersMsg(int count) {
        return MIN_CHARACTERS_MSG + count + " lowercase characters";
    }

    public static String getPasswordMaxLowerCaseCharactersMsg(int count) {
        return MAX_CHARACTERS_MSG + count + " lowercase characters";
    }

    public static String getPasswordMinUpperCaseCharactersMsg(int count) {
        return MIN_CHARACTERS_MSG+ count + " uppercase characters";
    }

    public static String getPasswordMaxUpperCaseCharactersMsg(int count) {
        return MAX_CHARACTERS_MSG + count + " uppercase characters";
    }

    public static String getPasswordMinNumericCharactersMsg(int count) {
        return MIN_CHARACTERS_MSG + count + " numeric characters";
    }

    public static String getPasswordMaxNumericCharactersMsg(int count) {
        return MAX_CHARACTERS_MSG + count + " numeric characters";
    }

    public static String getPasswordMinSpecialCharactersMsg(int count) {
        return MIN_CHARACTERS_MSG + count + " special characters";
    }

    public static String getPasswordMaxSpecialCharactersMsg(int count) {
        return MAX_CHARACTERS_MSG + count + " special characters";
    }


}
