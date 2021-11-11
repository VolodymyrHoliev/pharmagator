package com.eleks.academy.pharmagator.exceptions;

public class NotNullConstraintViolationException extends RuntimeException{

    public NotNullConstraintViolationException(String message) {
        super(message);
    }
}
