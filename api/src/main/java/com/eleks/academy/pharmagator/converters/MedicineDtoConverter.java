package com.eleks.academy.pharmagator.converters;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;

public interface MedicineDtoConverter<T> {
    T toEntity(MedicineDto dto);
}
