package com.eleks.academy.pharmagator.dataproviders.dto.aptslav.converters;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.dataproviders.dto.aptslav.AptslavMedicineDto;
import com.eleks.academy.pharmagator.dataproviders.dto.aptslav.AptslavPriceDto;
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

