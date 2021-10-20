package com.eleks.academy.pharmagator.repositories;

import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.entities.PriceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PriceRepository extends JpaRepository<Price, PriceId> {

    boolean existsByMedicineIdAndPharmacyId(Long medicineId, Long pharmacyId);

    <T> Optional<T> deleteByMedicineIdAndPharmacyId(Long medicineId,
                                                    Long pharmacyId, Class<T> returnType);

    Optional<Pharmacy> findByName(String title);

    <T> List<T> findAll(Class<T> returnType);

    <T> Optional<T> findAllByMedicineIdAndPharmacyId(Long medicineId,
                                                     Long pharmacyId, Class<T> returnType);


}
