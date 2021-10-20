package com.eleks.academy.pharmagator.controllers.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyRequest {

    @NotBlank (message = "'title' param is mandatory")
    private String title;

    @NotBlank (message = "'medicineLinkTemplate' param is mandatory")
    private String medicineLinkTemplate;
}
