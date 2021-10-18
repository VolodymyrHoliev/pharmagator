package com.eleks.academy.pharmagator.converters.medicineDtoToEntitiConverters;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Price;
import org.modelmapper.ModelMapper;

import java.time.Instant;

public class MedicineDtoToPriceConverter implements MedicineDtoToEntityConverter<Price>{
    @Override
    public Price getEntity(MedicineDto medicineDto) {
        Price price = new ModelMapper().map(medicineDto, Price.class);
        price.setUpdatedAt(Instant.now());
        return price;
    }
}
