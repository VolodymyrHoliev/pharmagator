package com.eleks.academy.pharmagator.converters.request;

import com.eleks.academy.pharmagator.controllers.requests.PharmacyRequest;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class PharmacyRequestMapper implements RequestToEntityConverter<PharmacyRequest, Pharmacy> {

    @Override
    public Pharmacy toEntity(@NotNull PharmacyRequest requestObject) {
        if(requestObject == null){
            throw new IllegalArgumentException("PharmacyRequest can`t be null");
        }
        String title = requestObject.getTitle();

        if(title == null || title.isBlank()){
            throw new IllegalArgumentException("\"title\" in PharmacyRequest must be not null and not blank");
        }

        String medicineLinkTemplate = requestObject.getMedicineLinkTemplate();

        Pharmacy pharmacy = new Pharmacy();

        pharmacy.setName(title);

        pharmacy.setMedicineLinkTemplate(medicineLinkTemplate);

        return pharmacy;
    }
}
