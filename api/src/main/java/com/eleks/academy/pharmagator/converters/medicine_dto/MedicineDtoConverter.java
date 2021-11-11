package com.eleks.academy.pharmagator.converters.medicine_dto;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;

public interface MedicineDtoConverter<T> {

    T toEntity(MedicineDto dto);
}
