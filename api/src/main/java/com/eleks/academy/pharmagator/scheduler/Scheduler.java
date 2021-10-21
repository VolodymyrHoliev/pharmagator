package com.eleks.academy.pharmagator.scheduler;

import com.eleks.academy.pharmagator.dataproviders.DataProvider;
import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.services.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
@Profile("!test")
public class Scheduler {

    private final List<DataProvider> dataProviders;

    private SchedulerService schedulerService;

    @Scheduled(fixedDelay = 120, timeUnit = TimeUnit.SECONDS)
    public void schedule() {
        //TODO
    }

    private void storeToDatabase(MedicineDto dto, Pharmacy pharmacy) {

        schedulerService.storeMedicineDto(dto, pharmacy);
    }
}
