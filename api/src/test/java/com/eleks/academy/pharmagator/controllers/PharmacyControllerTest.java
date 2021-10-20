package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.projections.PharmacyDto;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PharmacyControllerTest {

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private PharmacyController pharmacyController;

    @Test
    public void findAllPharmacies_returnsListOfPharmacyDto() {

        List<PharmacyDto> allPharmacies = pharmacyRepository.findAllPharmacies(PharmacyDto.class);

        assertEquals(1,allPharmacies.size());

        PharmacyDto pharmacyDto = allPharmacies.get(0);

        System.out.println(pharmacyDto.getName()+" "+pharmacyDto.getMedicineLinkTemplate());
    }
}