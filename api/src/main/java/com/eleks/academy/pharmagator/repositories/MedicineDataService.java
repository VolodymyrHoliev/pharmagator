package com.eleks.academy.pharmagator.repositories;

import com.eleks.academy.pharmagator.converters.DtoMapper;
import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.entities.Price;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicineDataService {
    private final PharmacyRepository pharmacyRepository;
    private final MedicineRepository medicineRepository;
    private final PriceRepository priceRepository;
    private final DtoMapper dtoMapper;

    public void saveMedicine(Medicine medicine) {
        medicineRepository.save(medicine);
    }

    public void saveMedicine(MedicineDto medicineDto) {
        var medicine = dtoMapper.toMedicineEntity(medicineDto);
        medicineRepository.save(medicine);
    }

    public void savePrice(Price price) {
        priceRepository.save(price);
    }

    public void savePrice(MedicineDto medicineDto) {
        var price = dtoMapper.toPriceEntity(medicineDto);
        priceRepository.save(price);
    }

    public boolean pharmacyExists(Pharmacy pharmacy) {
        return pharmacyRepository.existsById(pharmacy.getId());
    }

    public void savePharmacy(Pharmacy pharmacy) {
        pharmacyRepository.save(pharmacy);
    }
}
