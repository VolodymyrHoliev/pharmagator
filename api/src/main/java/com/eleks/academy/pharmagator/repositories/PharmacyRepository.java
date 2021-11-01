package com.eleks.academy.pharmagator.repositories;

import com.eleks.academy.pharmagator.entities.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
    boolean existsById(long id);

    <T> Optional<T> findByName(String title, Class<T> returnType);

    Optional<Pharmacy> findByName(String title);

    @Query("SELECT pharmacy FROM Pharmacy pharmacy")
    <T> List<T> findAllPharmacies(Class<T> returnType);

    <T> Optional<T> findById(Long id, Class<T> returnType);
}
