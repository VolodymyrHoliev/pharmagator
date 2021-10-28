package com.eleks.academy.pharmagator;

import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PharmagatorApplicationTests {

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Test
    void contextLoads() {


    }

    @Test
    public void test(){

        List<Pharmacy> all = pharmacyRepository.findAll();

        assertEquals(1,all.size());
    }

}
