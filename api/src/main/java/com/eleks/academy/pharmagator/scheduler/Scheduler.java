package com.eleks.academy.pharmagator.scheduler;

import com.eleks.academy.pharmagator.dataproviders.DataProvider;
import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.repositories.MedicineDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Component
public class Scheduler {
    private final List<DataProvider> dataProviders;
    private final MedicineDataService medicineDataService;

    @Scheduled(fixedDelay = 120, timeUnit = TimeUnit.SECONDS)
    public void schedule() {
        dataProviders.forEach(dataProvider -> {
            Stream<MedicineDto> medicineDtoStream = dataProvider.loadData();
            medicineDtoStream
                    .forEach(this::storeToDatabase);
        });
    }

    private void storeToDatabase(MedicineDto dto) {

//        medicineDataService.saveMedicine(dto);
//        medicineDataService.savePrice(dto);
    }
}
