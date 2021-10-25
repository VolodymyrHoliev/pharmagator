package com.eleks.academy.pharmagator.converters.request;

import com.eleks.academy.pharmagator.controllers.requests.PharmacyRequest;
import com.eleks.academy.pharmagator.entities.Pharmacy;
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
    void toEntity_null_NullPointerException() {

        assertThrows(NullPointerException.class, () -> subject.toEntity(null));
    }

    @Test
    void toEntity_nullTitleInPharmacyRequest_IllegalArgumentException(){

        PharmacyRequest request = new PharmacyRequest(null, "link");

        assertThrows(IllegalArgumentException.class,() -> subject.toEntity(request));
    }

    @Test
    void toEntity_blankTitleInPharmacyRequest_IllegalArgumentException(){

        PharmacyRequest request = new PharmacyRequest("  ", "link");

        assertThrows(IllegalArgumentException.class,() -> subject.toEntity(request));
    }

}