package com.eleks.academy.pharmagator.dataproviders.aptslav.dto.converters;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;

public interface ApiDtoConverter<T> {
    MedicineDto toMedicineDto(T apiDto);
}
