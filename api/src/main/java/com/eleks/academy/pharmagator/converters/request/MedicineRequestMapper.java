package com.eleks.academy.pharmagator.converters.request;

import com.eleks.academy.pharmagator.controllers.requests.MedicineRequest;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.exceptions.NotNullConstraintViolationException;
import org.springframework.stereotype.Component;

@Component
public class MedicineRequestMapper implements RequestToEntityConverter<MedicineRequest, Medicine> {

    @Override
    public Medicine toEntity(MedicineRequest requestObject) {
        if (requestObject == null) {
            throw new NotNullConstraintViolationException("MedicineRequest can`t be null");
        }

        Medicine medicine = new Medicine();

        medicine.setTitle(requestObject.getTitle());

        return medicine;
    }
}
