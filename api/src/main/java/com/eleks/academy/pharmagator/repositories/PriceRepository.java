package com.eleks.academy.pharmagator.repositories;

import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.entities.PriceId;
import com.eleks.academy.pharmagator.projections.PriceDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PriceRepository extends JpaRepository<Price, PriceId> {

    boolean existsByMedicineIdAndPharmacyId(Long medicineId, Long pharmacyId);

    <T> Optional<T> deleteByMedicineIdAndPharmacyId(Long medicineId,
                                                    Long pharmacyId, Class<T> returnType);
    @Query("SELECT price FROM Price price")
    <T> List<T> findAll(Class<T> returnType);

    <T> Optional<T> findByMedicineIdAndPharmacyId(Long medicineId,
                                                  Long pharmacyId, Class<T> returnType);


}
