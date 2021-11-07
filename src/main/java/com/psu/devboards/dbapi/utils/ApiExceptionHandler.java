package com.psu.devboards.dbapi.utils;

import com.psu.devboards.dbapi.exceptions.UniqueViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UniqueViolationException.class)
    protected ResponseEntity<Object> handleConflict(RuntimeException ex) {
        return generateErrorResponse(Collections.singletonList(ex.getMessage()), HttpStatus.CONFLICT);
    }

    /**
     * Overrides the default handler for when @Valid fails, this is useful when we want to enforce certain request
     * parameters
     **/
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        super.handleMethodArgumentNotValid(ex, headers, status, request);

        /* Get the error messages returned from our failed validations */
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        /* Convert FieldErrors list to list of strings to return in body */
        List<String> errors = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        return generateErrorResponse(errors, headers, status);
    }

    private ResponseEntity<Object> generateErrorResponse(List<String> errors, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();

        return generateErrorResponse(errors, headers, status);
    }

    private ResponseEntity<Object> generateErrorResponse(List<String> errors, HttpHeaders headers, HttpStatus status) {
        /* Create exception body that will hold the response parameters */
        HashMap<String, Object> body = new LinkedHashMap<>();

        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);
    }
}

