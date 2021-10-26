package com.eleks.academy.pharmagator.controllers.advices;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class PharmacyControllerAdvice {

    @ExceptionHandler(value = {ResponseStatusException.class})
    protected ResponseEntity<Map<String, Object>> handle(ResponseStatusException e) {

        Map<String, Object> responseBody = new HashMap<>();

        String message = e.getReason();

        HttpStatus status = e.getStatus();

        responseBody.put("errorMessage", message);

        responseBody.put("status", status);

        responseBody.put("statusCode", e.getRawStatusCode());

        return ResponseEntity.badRequest().body(responseBody);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Map<String, Object>> handle(MethodArgumentNotValidException e) {

        //TODO make error response in this method more informative

        Map<String, Object> responseBody = new HashMap<>();

        responseBody.put("errors", e.getMessage());

        return ResponseEntity.badRequest().body(responseBody);
    }

    @ExceptionHandler({IllegalArgumentException.class, EmptyResultDataAccessException.class,
            ConstraintViolationException.class})
    protected ResponseEntity<Map<String, Object>> handle(Exception e) {

        Map<String, Object> responseBody = new HashMap<>();

        responseBody.put("errorMessage", e.getLocalizedMessage());

        return ResponseEntity.badRequest().body(responseBody);
    }
}
