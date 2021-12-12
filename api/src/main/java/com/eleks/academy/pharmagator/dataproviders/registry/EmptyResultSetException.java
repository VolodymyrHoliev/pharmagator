package com.eleks.academy.pharmagator.dataproviders.registry;

public class EmptyResultSetException extends RuntimeException {

    public EmptyResultSetException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyResultSetException(String message) {
        super(message);
    }

}
