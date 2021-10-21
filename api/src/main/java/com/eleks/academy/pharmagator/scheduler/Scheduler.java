package com.eleks.academy.pharmagator.scheduler;

import com.eleks.academy.pharmagator.converters.medicine_dto.MedicineDtoMapper;
import com.eleks.academy.pharmagator.dataproviders.PharmacyDataProvider;
import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class Scheduler {
    private final List<PharmacyDataProvider> dataProviders;
    private PharmacyRepository pharmacyRepository;
    private MedicineRepository medicineRepository;
    private PriceRepository priceRepository;
    private MedicineDtoMapper mapper;

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
        Medicine medicine = mapper.toMedicineEntity(dto);
        medicineRepository.saveAndFlush(medicine);
        Price price = mapper.toPriceEntity(dto);

        price.setPharmacyId(pharmacy.getId());
        price.setMedicineId(medicine.getId());
        priceRepository.save(price);
    }
}
