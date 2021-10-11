package com.eleks.academy.pharmagator.repositories;

import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.entities.PriceId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PriceRepository extends JpaRepository<Price, PriceId> {
    Optional<Price> findByMedicineIdAndPharmacyId(long medicineId, long pharmacyId);
}
