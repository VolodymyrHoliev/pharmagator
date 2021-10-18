package com.eleks.academy.pharmagator.dataproviders.aptslav.dto.converters;

import com.eleks.academy.pharmagator.dataproviders.aptslav.dto.MedicineApiDto;
import com.eleks.academy.pharmagator.dataproviders.aptslav.dto.PriceDto;
import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.sun.istack.NotNull;
import org.springframework.stereotype.Service;

@Service
public class ApiMedicineDtoConverter implements ApiDtoConverter<MedicineApiDto> {
    @Override
    public MedicineDto toMedicineDto(@NotNull MedicineApiDto apiDto) {
        String title = apiDto.getName();
        PriceDto priceDto = apiDto.getPrice();
        long externalId = apiDto.getExternalId();
        return MedicineDto.builder()
                .externalId(String.valueOf(externalId))
                .title(title)
                .price(priceDto.getMin())
                .build();
    }
}
