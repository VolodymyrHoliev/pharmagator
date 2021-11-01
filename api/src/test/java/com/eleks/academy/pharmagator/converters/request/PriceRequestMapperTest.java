package com.eleks.academy.pharmagator.converters.request;

import com.eleks.academy.pharmagator.controllers.requests.MedicineRequest;
import com.eleks.academy.pharmagator.controllers.requests.PriceRequest;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.exceptions.NotNullConstraintViolationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PriceRequestMapperTest {

    private final PriceRequestMapper subject = new PriceRequestMapper();

    @Test
    void toEntity_ok(){

        final String expectedExtId = "ExternalId";

        final BigDecimal expectedPriceValue = new BigDecimal("20.50");

        PriceRequest priceRequest = new PriceRequest(expectedPriceValue, expectedExtId);

        Price entity = subject.toEntity(priceRequest);

        assertEquals(expectedExtId, entity.getExternalId());

        assertEquals(expectedPriceValue, entity.getPrice());

        assertEquals(0L, entity.getPharmacyId());

        assertEquals(0L, entity.getMedicineId());

    }

    @Test
    void toEntity_null_NotNullConstrainViolationException(){

        assertThrows(NotNullConstraintViolationException.class, () -> subject.toEntity(null));
    }

}