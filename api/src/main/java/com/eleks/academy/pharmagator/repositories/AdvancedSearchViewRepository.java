package com.eleks.academy.pharmagator.repositories;

import com.eleks.academy.pharmagator.entities.AdvancedSearchView;
import com.eleks.academy.pharmagator.entities.PriceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvancedSearchViewRepository extends JpaRepository<AdvancedSearchView, PriceId>,
                                                      JpaSpecificationExecutor<AdvancedSearchView> {
}
