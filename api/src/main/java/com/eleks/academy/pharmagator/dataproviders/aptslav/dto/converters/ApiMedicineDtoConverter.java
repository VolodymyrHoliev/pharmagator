package com.eleks.academy.pharmagator.dataproviders.aptslav.dto.converters;

import com.eleks.academy.pharmagator.dataproviders.aptslav.dto.AptslavMedicineDto;
import com.eleks.academy.pharmagator.dataproviders.aptslav.dto.AptslavPriceDto;
import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.sun.istack.NotNull;
import org.springframework.stereotype.Service;

@Service
public class ApiMedicineDtoConverter implements ApiDtoConverter<AptslavMedicineDto> {

    @Override
    public MedicineDto toMedicineDto(@NotNull AptslavMedicineDto apiDto) {

        String title = apiDto.getName();

        AptslavPriceDto aptslavPriceDto = apiDto.getPrice();

        long externalId = apiDto.getId();

        return MedicineDto.builder()
                .externalId(String.valueOf(externalId))
                .title(title)
                .price(aptslavPriceDto.getMin())
                .build();

    }

}

