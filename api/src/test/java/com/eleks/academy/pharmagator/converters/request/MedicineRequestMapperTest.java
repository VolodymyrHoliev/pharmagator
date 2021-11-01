package com.eleks.academy.pharmagator.converters.request;

import com.eleks.academy.pharmagator.controllers.requests.MedicineRequest;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.exceptions.NotNullConstraintViolationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MedicineRequestMapperTest {

    private final MedicineRequestMapper subject = new MedicineRequestMapper();

    @Test
    void toEntity_ok(){

        final String expectedTitle = "Some title";

        MedicineRequest medicineRequest = new MedicineRequest(expectedTitle);

        Medicine entity = subject.toEntity(medicineRequest);

        assertEquals(expectedTitle, entity.getTitle());

        assertEquals(0L, entity.getId());

    }

    @Test
    void toEntity_null_NotNullConstrainViolationException(){

        assertThrows(NotNullConstraintViolationException.class, () -> subject.toEntity(null));
    }
}