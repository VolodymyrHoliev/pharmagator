package com.eleks.academy.pharmagator.converters.request;

import com.eleks.academy.pharmagator.controllers.requests.MedicineRequest;
import com.eleks.academy.pharmagator.entities.Medicine;
import org.springframework.stereotype.Component;

@Component
public class MedicineRequestMapper implements RequestToEntityConverter<MedicineRequest, Medicine> {

    @Override
    public Medicine toEntity(MedicineRequest requestObject) {
        if (requestObject == null) {
            throw new IllegalArgumentException("PharmacyRequest can`t be null");
        }

        Medicine medicine = new Medicine();

        medicine.setTitle(requestObject.getTitle());

        return medicine;
    }
}
