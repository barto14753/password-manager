package com.example.passwordmanager.exception;

public class ExceptionMessage {

    // AUTHENTICATION EXCEPTIONS
    public static String getUserNotExistMsg(String email) {
        return "User with email " + email + " not exist";
    }

    public static final String ACCESS_TOKEN_EXPIRED = "Access token has expired";
    public static final String REFRESH_TOKEN_EXPIRED = "Refresh token has expired, account will be locked";
    public static final String ACCOUNT_LOCKED = "Cannot authenticate since account is locked, you need to login";
    public static final String WRONG_TOKEN_TYPE_FOR_REFRESH = "You have to use refresh token for this action";
    public static final String WRONG_TOKEN_TYPE_FOR_ACCESS = "You have to use access token for this action";
    public static final String WRONG_HEADER = "You need to provide token inside header";

    // REGISTER EXCEPTIONS
    public static String getEmailTakenMsg(String email) {
        return "User with email " + email + " already exist";
    }

    public static String getEmailInvalidMsg(String email) {
        return "Email " + email + " is not valid";
    }

    public static String getPasswordMinCharactersMsg(int count) {
        return "Password needs to have minimum " + count + " characters";
    }

    public static String getPasswordMaxCharactersMsg(int count) {
        return "Password needs to have maximum " + count + " characters";
    }

    public static String getPasswordMinLowerCaseCharactersMsg(int count) {
        return "Password needs to have minimum " + count + " lowercase characters";
    }

    public static String getPasswordMaxLowerCaseCharactersMsg(int count) {
        return "Password needs to have maximum " + count + " lowercase characters";
    }

    public static String getPasswordMinUpperCaseCharactersMsg(int count) {
        return "Password needs to have minimum " + count + " uppercase characters";
    }

    public static String getPasswordMaxUpperCaseCharactersMsg(int count) {
        return "Password needs to have maximum " + count + " uppercase characters";
    }

    public static String getPasswordMinNumericCharactersMsg(int count) {
        return "Password needs to have minimum " + count + " numeric characters";
    }

    public static String getPasswordMaxNumericCharactersMsg(int count) {
        return "Password needs to have maximum " + count + " numeric characters";
    }

    public static String getPasswordMinSpecialCharactersMsg(int count) {
        return "Password needs to have minimum " + count + " special characters";
    }

    public static String getPasswordMaxSpecialCharactersMsg(int count) {
        return "Password needs to have maximum " + count + " special characters";
    }


}
