package com.eleks.academy.pharmagator.dataproviders.aptslav.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceDto{
    private BigDecimal max;
    private BigDecimal min;
}
