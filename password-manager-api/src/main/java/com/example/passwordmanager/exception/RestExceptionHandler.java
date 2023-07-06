package com.example.passwordmanager.exception;

import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.ServletException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.TimeLimitExceededException;
import java.io.IOException;
import java.text.ParseException;

import static org.springframework.http.HttpStatus.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFound(UsernameNotFoundException ex) {
        return handleExceptionWithStatusCode(NOT_FOUND, ex);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<Object> handleSignatureException(SignatureException ex) {
        return handleExceptionWithStatusCode(FORBIDDEN, ex);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Object> handleIOException(IOException ex) {
        return handleExceptionWithStatusCode(INTERNAL_SERVER_ERROR, ex);
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<Object> handleServletException(ServletException ex) {
        return handleExceptionWithStatusCode(INTERNAL_SERVER_ERROR, ex);
    }

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<Object> handleDateTimeParseException(ParseException ex) {
        return handleExceptionWithStatusCode(BAD_REQUEST, ex);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Object> handleDateTimeParseException(AuthException ex) {
        return handleExceptionWithStatusCode(BAD_REQUEST, ex);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleDateTimeParseException(BadCredentialsException ex) {
        return handleExceptionWithStatusCode(BAD_REQUEST, ex);
    }

    @ExceptionHandler(RegisterException.class)
    public ResponseEntity<Object> handleDateTimeParseException(RegisterException ex) {
        return handleExceptionWithStatusCode(BAD_REQUEST, ex);
    }

    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<Object> handleDateTimeParseException(PasswordException ex) {
        return handleExceptionWithStatusCode(BAD_REQUEST, ex);
    }

    @ExceptionHandler(PasswordResetException.class)
    public ResponseEntity<Object> handleDateTimeParseException(PasswordResetException ex) {
        return handleExceptionWithStatusCode(BAD_REQUEST, ex);
    }

    @ExceptionHandler(TimeLimitExceededException.class)
    public ResponseEntity<Object> handleTimeLimitExceededException(TimeLimitExceededException ex) {
        return handleExceptionWithStatusCode(REQUEST_TIMEOUT, ex);
    }

    private ResponseEntity<Object> handleExceptionWithStatusCode(HttpStatus httpStatus, Exception ex) {
        ApiError apiError = new ApiError(httpStatus);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}