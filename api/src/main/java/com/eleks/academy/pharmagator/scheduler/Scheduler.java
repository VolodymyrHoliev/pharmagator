package com.eleks.academy.pharmagator.scheduler;

import com.eleks.academy.pharmagator.converters.DtoMapper;
import com.eleks.academy.pharmagator.converters.MedicineDtoConverter;
import com.eleks.academy.pharmagator.dataproviders.DataProvider;
import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Component
public class Scheduler {
    private final List<DataProvider> dataProviders;
    private final DtoMapper dtoMapper;
    private final MedicineRepository medicineRepository;
    private final PriceRepository priceRepository;

    @Scheduled(fixedDelay = 120, timeUnit = TimeUnit.SECONDS)
    public void schedule() {
        dataProviders.forEach(dataProvider -> {
            Stream<MedicineDto> medicineDtoStream = dataProvider.loadData();
            medicineDtoStream
                    .forEach(this::storeToDatabase);
        });
    }

    private void storeToDatabase(MedicineDto dto) {
        Price price = dtoMapper.toPriceEntity(dto);
        Medicine medicine = dtoMapper.toMedicineEntity(dto);
//            medicineRepository.save(medicine);
//            priceRepository.save(price);
    }
}
