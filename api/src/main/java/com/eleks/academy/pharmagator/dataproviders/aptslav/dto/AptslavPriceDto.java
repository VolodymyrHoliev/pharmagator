package com.eleks.academy.pharmagator.dataproviders.aptslav.dto;
/**
 * @author Marharyta Levtsun
 * @see <a href = "https://github.com/ritalinch"</a>
 */

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AptslavPriceDto {

    private BigDecimal max;

    private BigDecimal min;

}
