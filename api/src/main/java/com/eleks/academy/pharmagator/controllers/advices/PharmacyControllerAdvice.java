package com.eleks.academy.pharmagator.controllers.advices;

import com.eleks.academy.pharmagator.exceptions.ObjectNotFoundException;
import com.eleks.academy.pharmagator.validation.ValidationErrorResponse;
import com.eleks.academy.pharmagator.validation.Violation;
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

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ControllerAdvice
public class PharmacyControllerAdvice {

    @ExceptionHandler(value = {ObjectNotFoundException.class})
    protected ResponseEntity<Map<String, Object>> handle(ObjectNotFoundException e) {

        Map<String, Object> responseBody = new HashMap<>();

        responseBody.put("errorMessage", e.getMessage());

        responseBody.put("status", "Not found");

        responseBody.put("statusCode", 404);

        return ResponseEntity.badRequest().body(responseBody);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ValidationErrorResponse> handle(MethodArgumentNotValidException e) {

        ValidationErrorResponse errorResponse = new ValidationErrorResponse();

        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        for (FieldError error : fieldErrors) {

            Violation violation = Violation.builder()
                    .fieldName(error.getField())
                    .errorMessage(error.getDefaultMessage())
                    .build();

            errorResponse.addViolation(violation);
        }
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    protected ResponseEntity<ValidationErrorResponse> handle(ConstraintViolationException e) {

        ValidationErrorResponse errorResponse = new ValidationErrorResponse();

        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

        for (ConstraintViolation<?> constraintViolation : constraintViolations) {

            Violation violation = Violation.builder()
                    .fieldName(constraintViolation.getPropertyPath().toString())
                    .errorMessage(constraintViolation.getMessage())
                    .build();

            errorResponse.addViolation(violation);
        }

        return ResponseEntity.badRequest().body(errorResponse);
    }
}
