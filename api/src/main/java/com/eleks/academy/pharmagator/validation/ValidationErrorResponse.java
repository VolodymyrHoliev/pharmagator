package com.eleks.academy.pharmagator.validation;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
public class ValidationErrorResponse {

    private final List<Violation> violations = new ArrayList<>();

    public void addViolation(Violation violation){

        this.violations.add(violation);
    }
}
