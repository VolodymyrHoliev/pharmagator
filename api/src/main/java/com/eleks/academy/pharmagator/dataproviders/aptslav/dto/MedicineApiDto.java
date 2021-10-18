package com.eleks.academy.pharmagator.dataproviders.aptslav.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class MedicineApiDto {
    private long id;
    private long externalId;
    private String name;
    private PriceDto price;
    private Instant created;

}
