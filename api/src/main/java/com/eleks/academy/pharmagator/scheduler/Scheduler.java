package com.eleks.academy.pharmagator.scheduler;

import com.eleks.academy.pharmagator.converters.medicine_dto.MedicineDtoMapper;
import com.eleks.academy.pharmagator.dataproviders.PharmacyDataProvider;
import com.eleks.academy.pharmagator.dataproviders.aptslav.AptslavDataProvider;
import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import com.eleks.academy.pharmagator.services.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class Scheduler {

    private final List<PharmacyDataProvider> dataProviders;

    private SchedulerService schedulerService;

    @Scheduled(fixedDelay = 120, timeUnit = TimeUnit.SECONDS)
    public void schedule() {
//        dataProviders.forEach(dataProvider -> {
//            Stream<MedicineDto> medicineDtoStream = dataProvider.loadData();
//            Pharmacy pharmacy = dataProvider.getPharmacy();
//            medicineDtoStream
//                    .forEach(medicineDto -> storeToDatabase(medicineDto, pharmacy));
//        });
    }

    private void storeToDatabase(MedicineDto dto, Pharmacy pharmacy) {

        schedulerService.storeMedicineDto(dto, pharmacy);
    }
}
