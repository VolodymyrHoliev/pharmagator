package com.eleks.academy.pharmagator.dataproviders.aptslav.dto;
/**
 * @author Marharyta Levtsun
 * @see <a href = "https://github.com/ritalinch"</a>
 */

import lombok.Data;

import java.time.Instant;

@Data
public class AptslavMedicineDto {

    private long id;

    private long externalId;

    private String name;

    private AptslavPriceDto price;

    private Instant created;

}
