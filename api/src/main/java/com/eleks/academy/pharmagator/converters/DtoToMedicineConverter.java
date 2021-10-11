package com.eleks.academy.pharmagator.converters;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import org.springframework.stereotype.Component;

@Component
public class DtoToMedicineConverter implements MedicineDtoConverter<Medicine> {
    @Override
    public Medicine toEntity(MedicineDto dto) {
        Medicine medicine = new Medicine();
        medicine.setTitle(dto.getTitle());
        return medicine;
    }
}
