package com.eleks.academy.pharmagator.parsers.exceptions;

public class UnsupportedModelTypeException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Can't map records from .csv to model with @ParsedCollection annotated fields\n" +
                "Consider use another parser or remove @ParsedCollection annotation";
    }
}
