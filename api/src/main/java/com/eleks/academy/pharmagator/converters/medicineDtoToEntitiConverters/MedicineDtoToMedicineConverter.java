package com.eleks.academy.pharmagator.converters.medicineDtoToEntitiConverters;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import org.modelmapper.ModelMapper;

public class MedicineDtoToMedicineConverter implements MedicineDtoToEntityConverter<Medicine>{
    @Override
    public Medicine getEntity(MedicineDto medicineDto) {
        return new ModelMapper().map(medicineDto, Medicine.class);
    }
}
