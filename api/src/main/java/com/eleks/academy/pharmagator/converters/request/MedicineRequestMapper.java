package com.eleks.academy.pharmagator.converters.request;

import com.eleks.academy.pharmagator.controllers.requests.MedicineRequest;
import com.eleks.academy.pharmagator.entities.Medicine;

public class MedicineRequestMapper implements RequestToEntityConverter<MedicineRequest, Medicine> {

    @Override
    public Medicine toEntity(MedicineRequest requestObject) {

        Medicine medicine = new Medicine();

        medicine.setTitle(requestObject.getTitle());

        return medicine;
    }
}
