package com.eleks.academy.pharmagator.converters.medicine_dto;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Price;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class DtoToPriceConverter implements MedicineDtoConverter<Price> {
    @Override
    public Price toEntity(MedicineDto dto) {

        Price price = new Price();

        price.setPrice(dto.getPrice());

        price.setExternalId(dto.getExternalId());

        price.setUpdatedAt(Instant.now());

        return price;

    }
}
