package com.example.clinic_management.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.NonNull;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(
            ResourceNotFoundException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(), exception.getMessage(), request.getDescription(false), "NOT_FOUND");

        logger.severe("Logger Resource not found exception: " + exception.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyExistedException.class)
    public ResponseEntity<ErrorDetails> handleEmailAlreadyExistedException(
            EmailAlreadyExistedException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(), exception.getMessage(), request.getDescription(false), "BAD_REQUEST");

        logger.severe("Email already existed exception: " + exception.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(PatientNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handlePatientNotFoundException(PatientNotFoundException ex) {
//        ErrorResponse error = new ErrorResponse(
//                HttpStatus.NOT_FOUND.value(),
//                "Patient Not Found",
//                ex.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(PrescriptionValidationException.class)
//    public ResponseEntity<ErrorResponse> handlePrescriptionValidationException(PrescriptionValidationException ex) {
//        ErrorResponse error = new ErrorResponse(
//                HttpStatus.BAD_REQUEST.value(),
//                "Prescription Validation Error",
//                ex.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(), exception.getMessage(), request.getDescription(false), "INTERNAL_SERVER_ERROR");

        logger.severe("Internal server error: " + exception.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // For handle, when the request is not valid, using @Valid and java validation
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        Map<String, String> listErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            listErrors.put(error.getField(), error.getDefaultMessage());
        });

        logger.severe("Method argument not valid exception: " + listErrors);
        return new ResponseEntity<>(listErrors, HttpStatus.BAD_REQUEST);
    }
    // Handle invalid type of enum request
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            WebRequest request) {
        ErrorDetails errorDetails =
                new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false), "BAD_REQUEST");

        logger.severe("Http message not readable exception: " + ex.getMessage());
        return new ResponseEntity<>(errorDetails, status);
    }
}
