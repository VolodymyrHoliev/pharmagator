package com.eleks.academy.pharmagator.scheduler;

import com.eleks.academy.pharmagator.converters.MedicineDtoConverter;
import com.eleks.academy.pharmagator.dataproviders.DataProvider;
import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class Scheduler {
    private final DataProvider dataProvider;
    private final MedicineDtoConverter<Price> dtoToPriceConverter;
    private final MedicineDtoConverter<Medicine> dtoToMedicineConverter;
    private final MedicineRepository medicineRepository;
    private final PriceRepository priceRepository;

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void schedule() {
        log.info("Scheduler started at {}", Instant.now());
        dataProvider.loadData().forEach(this::storeToDatabase);
    }

    private void storeToDatabase(MedicineDto dto) {
        Price price = dtoToPriceConverter.toEntity(dto);
        Medicine medicine = dtoToMedicineConverter.toEntity(dto);
//            medicineRepository.save(medicine);
//            priceRepository.save(price);
    }
}
