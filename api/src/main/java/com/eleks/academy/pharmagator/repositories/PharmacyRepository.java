package com.eleks.academy.pharmagator.repositories;

import com.eleks.academy.pharmagator.entities.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
    boolean existsById(long id);

    Optional<Pharmacy> findByName(String title);
}
