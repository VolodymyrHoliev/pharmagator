package com.eleks.academy.pharmagator.repositories;

import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.entities.PriceId;
import com.eleks.academy.pharmagator.projections.MedicinePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PriceRepository extends JpaRepository<Price, PriceId> {
    @Query("""
            SELECT p.price as price, m.title as title, p.pharmacyId as pharmacyId
            FROM Price p
            LEFT JOIN Medicine m ON m.id = p.medicineId
            """)
    List<MedicinePrice> findAllMedicinesPrices();
}
