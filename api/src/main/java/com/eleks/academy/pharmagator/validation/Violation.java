package com.eleks.academy.pharmagator.validation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Violation {

    private String fieldName;

    private String errorMessage;
}
