package com.eleks.academy.pharmagator.controllers.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class PharmacyControllerAdvice {

    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<Map<String,Object>> handle(ResponseStatusException e){

        Map<String,Object> responseBody = new HashMap<>();

        String message = e.getReason();

        HttpStatus status = e.getStatus();

        responseBody.put("errorMessage", message);

        responseBody.put("status", status);

        responseBody.put("statusCode",e.getRawStatusCode());

        return ResponseEntity.badRequest().body(responseBody);
    }
}
