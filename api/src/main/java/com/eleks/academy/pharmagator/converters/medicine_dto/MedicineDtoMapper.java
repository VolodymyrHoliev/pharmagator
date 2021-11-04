package com.eleks.academy.pharmagator.converters.medicine_dto;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.entities.Price;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicineDtoMapper {
    private final MedicineDtoConverter<Medicine> medicineConverter;
    private final MedicineDtoConverter<Price> priceConverter;

    public Medicine toMedicineEntity(MedicineDto medicineDto) {
        return medicineConverter.toEntity(medicineDto);
    }

    public Price toPriceEntity(MedicineDto medicineDto) {
        return priceConverter.toEntity(medicineDto);
    }
}
