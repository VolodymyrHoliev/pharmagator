package com.eleks.academy.pharmagator.controllers.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceRequest {

    @NotNull
    @Positive
    @Min(value = 1)
    private BigDecimal price;

    @NotBlank
    private String externalId;
}
