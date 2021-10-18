package com.eleks.academy.pharmagator.converters.medicineDtoToEntitiConverters;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;

public interface MedicineDtoToEntityConverter<T> {
    T getEntity (MedicineDto medicineDto);
}
