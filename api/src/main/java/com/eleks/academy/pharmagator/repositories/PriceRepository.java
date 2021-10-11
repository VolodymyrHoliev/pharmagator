package com.eleks.academy.pharmagator.repositories;

import com.eleks.academy.pharmagator.entities.Price;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price, Long> {
}
