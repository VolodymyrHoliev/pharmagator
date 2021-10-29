package com.eleks.academy.pharmagator.converters.request;

import com.eleks.academy.pharmagator.controllers.requests.PharmacyRequest;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.exceptions.NotNullConstraintViolationException;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Component
public class PharmacyRequestMapper implements RequestToEntityConverter<PharmacyRequest, Pharmacy> {

    @Override
    public Pharmacy toEntity(@Valid PharmacyRequest requestObject) {
        if(requestObject == null){
            throw new NotNullConstraintViolationException("PharmacyRequest can`t be null");
        }
        String title = requestObject.getTitle();

        String medicineLinkTemplate = requestObject.getMedicineLinkTemplate();

        Pharmacy pharmacy = new Pharmacy();

        pharmacy.setName(title);

        pharmacy.setMedicineLinkTemplate(medicineLinkTemplate);

        return pharmacy;
    }
}
