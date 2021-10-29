package com.eleks.academy.pharmagator.exceptions;

public class UniqueConstraintViolation extends RuntimeException {

    public UniqueConstraintViolation(String message) {
        super(message);
    }
}
