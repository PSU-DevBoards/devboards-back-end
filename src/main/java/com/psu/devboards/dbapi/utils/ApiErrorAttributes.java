package com.psu.devboards.dbapi.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.*;

@ControllerAdvice
public class ApiErrorAttributes extends ResponseEntityExceptionHandler {
    /** Overrides the default handler for when @Valid fails, this is useful when we want to enforce certain request parameters **/
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        super.handleMethodArgumentNotValid(ex, headers, status, request);

        /* Create exception body that will hold the response parameters */
        HashMap<String, Object> body = new LinkedHashMap<>();

        /* Get the error messages returned from our failed validations */
        List<FieldError> errList = ex.getBindingResult().getFieldErrors();

        /* Convert FieldErrors list to list of strings to return in body */
        ArrayList<String> errors = new ArrayList<>();
        for( FieldError error : errList ){
            errors.add( error.getDefaultMessage() );
        }

        /* Add attributes to response */
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("errors",  errors);

        return new ResponseEntity<>(body, headers, status);
    }

    public ResponseEntity<Object> generateErrorResponse(List<String> errors, HttpStatus status){
        HttpHeaders headers = new HttpHeaders();

        /* Create exception body that will hold the response parameters */
        HashMap<String, Object> body = new LinkedHashMap<>();

        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("errors",  errors);

        return new ResponseEntity<>(body, headers, status);
    }
}

