package com.kshirsa.coreservice.exception;

import com.kshirsa.coreservice.FailureResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import io.jsonwebtoken.security.SignatureException;
import jakarta.mail.MessagingException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<FailureResponse> customException(CustomException ex) {
        ErrorCode ec = ErrorCode.valueOf(ex.getMessage());
        ErrorDetails error = new ErrorDetails(ec.getErrorMessage(), null, ec.getErrorCode());
        return new ResponseEntity<>(new FailureResponse(false, ec.getErrorMessage(), error), HttpStatusCode.valueOf(ec.getHttpStatusCode()));
    }

    @ExceptionHandler
    public ResponseEntity<FailureResponse> allOtherException(Exception ex) {
        log.error(ex.getMessage(), ex);
        ErrorDetails error = new ErrorDetails(ErrorCode.GENERAL_EXCEPTION.getErrorMessage(),
                ex.getMessage(), ErrorCode.GENERAL_EXCEPTION.getErrorCode());
        return new ResponseEntity<>(new FailureResponse(false, error.getErrorMessage(), error), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<FailureResponse> userNameException(UsernameNotFoundException ex) {
        ErrorCode ec = ErrorCode.valueOf(ex.getMessage());
        ErrorDetails error = new ErrorDetails(ec.getErrorMessage(), null, ec.getErrorCode());
        return new ResponseEntity<>(new FailureResponse(false, ec.getErrorMessage(), error), HttpStatusCode.valueOf(ec.getHttpStatusCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<FailureResponse>> handleValidationException(MethodArgumentNotValidException ex) {
        List<FailureResponse> exception = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            ErrorCode ec = ErrorCode.valueOf(error.getDefaultMessage());
            ErrorDetails errorDetails = new ErrorDetails(ec.getErrorMessage(), null, ec.getErrorCode());
            exception.add(new FailureResponse(false, ec.getErrorMessage(), errorDetails));
        });
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<FailureResponse> constraintViolationException(ConstraintViolationException ex) {
        ErrorCode ec = ErrorCode.valueOf(ex.getMessage().split(":")[1].strip());
        ErrorDetails error = new ErrorDetails(ec.getErrorMessage(), null, ec.getErrorCode());
        return new ResponseEntity<>(new FailureResponse(false, ec.getErrorMessage(), error), HttpStatusCode.valueOf(ec.getHttpStatusCode()));
    }

    @ExceptionHandler
    public ResponseEntity<FailureResponse> expiredJwtException(ExpiredJwtException ex) {
        log.error(ex.getMessage());
        ErrorCode ec = ErrorCode.JWT_EXPIRED;
        ErrorDetails error = new ErrorDetails(ec.getErrorMessage(), null, ec.getErrorCode());
        return new ResponseEntity<>(new FailureResponse(false, ec.getErrorMessage(), error), HttpStatusCode.valueOf(ec.getHttpStatusCode()));
    }

    @ExceptionHandler
    public ResponseEntity<FailureResponse> jwtSignatureException(SignatureException ex) {
        log.warn(ex.getMessage());
        ErrorCode ec = ErrorCode.JWT_WRONG_SIGNATURE;
        ErrorDetails error = new ErrorDetails(ec.getErrorMessage(), null, ec.getErrorCode());
        return new ResponseEntity<>(new FailureResponse(false, ec.getErrorMessage(), error), HttpStatusCode.valueOf(ec.getHttpStatusCode()));
    }

    @ExceptionHandler
    public ResponseEntity<FailureResponse> messagingException(MessagingException ex) {
        ErrorDetails error = new ErrorDetails(ex.getMessage(), null, 100);
        return new ResponseEntity<>(new FailureResponse(false, ex.getMessage(), error), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<FailureResponse> missingHeaderException(MissingRequestHeaderException ex) {
        log.error(ex.getMessage(), ex);
        ErrorDetails error = new ErrorDetails(ErrorCode.MISSING_HEADER.getErrorMessage(), ex.getMessage(), ErrorCode.MISSING_HEADER.getErrorCode());
        return new ResponseEntity<>(new FailureResponse(false, error.getErrorMessage(), error), HttpStatus.BAD_REQUEST);
    }
}
