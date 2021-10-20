package com.eleks.academy.pharmagator.converters.request;

import com.eleks.academy.pharmagator.controllers.requests.PharmacyRequest;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import org.springframework.stereotype.Component;

@Component
public class PharmacyRequestMapper implements RequestToEntityConverter<PharmacyRequest, Pharmacy> {

    @Override
    public Pharmacy toEntity(PharmacyRequest requestObject) {

        String title = requestObject.getTitle();

        String medicineLinkTemplate = requestObject.getMedicineLinkTemplate();

        Pharmacy pharmacy = new Pharmacy();

        pharmacy.setName(title);

        pharmacy.setMedicineLinkTemplate(medicineLinkTemplate);

        return pharmacy;
    }
}
