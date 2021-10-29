package com.eleks.academy.pharmagator.converters.request;

import com.eleks.academy.pharmagator.controllers.requests.PharmacyRequest;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.exceptions.NotNullConstraintViolationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PharmacyRequestMapperTest {

    private final PharmacyRequestMapper subject = new PharmacyRequestMapper();

    @Test
    void toEntity_validInput_ok() {

        final String expectedTitle = "Pharmacy";
        final String expectedLinkTemplate = "linkTemplate";

        PharmacyRequest request = new PharmacyRequest(expectedTitle, expectedLinkTemplate);

        Pharmacy pharmacy = subject.toEntity(request);

        assertEquals(expectedTitle, pharmacy.getName());

        assertEquals(expectedLinkTemplate, pharmacy.getMedicineLinkTemplate());

        assertEquals(0L, pharmacy.getId());
    }

    @Test
    void toEntity_null_NotNullConstraintViolationException() {

        assertThrows(NotNullConstraintViolationException.class, () -> subject.toEntity(null));
    }

}