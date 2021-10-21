package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.converters.MedicineDtoMapper;
import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
@Validated
public class SchedulerService {

    private final MedicineRepository medicineRepository;

    private final PriceRepository priceRepository;

    private final MedicineDtoMapper mapper;

    public void storeMedicineDto(@Valid @NotNull MedicineDto dto, @Valid @NotNull Pharmacy pharmacy) {


        Medicine medicineEntity = mapper.toMedicineEntity(dto);

        medicineRepository.saveAndFlush(medicineEntity);

        Price price = mapper.toPriceEntity(dto);

        price.setPharmacyId(pharmacy.getId());

        price.setMedicineId(medicineEntity.getId());

        priceRepository.save(price);


    }
}
