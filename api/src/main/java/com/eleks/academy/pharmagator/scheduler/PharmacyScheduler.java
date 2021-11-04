package com.eleks.academy.pharmagator.scheduler;

import com.eleks.academy.pharmagator.dataproviders.PharmacyDataProvider;
import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor

public class PharmacyScheduler {

    private final List<PharmacyDataProvider> dataProvidersList;

    //TODO change fixedDelay if needed
    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void schedule() {

        dataProvidersList.forEach(dataProvider -> {

            Stream<MedicineDto> medicineDtoStream = dataProvider.loadData();

            Pharmacy pharmacy = dataProvider.getPharmacy();

            medicineDtoStream
                    .forEach(medicineDto -> storeToDatabase(medicineDto, pharmacy));
        });
    }

    private void storeToDatabase(MedicineDto medicineDto, Pharmacy pharmacy) {

        //TODO implement custom logic for saving data to the database
    }
}
