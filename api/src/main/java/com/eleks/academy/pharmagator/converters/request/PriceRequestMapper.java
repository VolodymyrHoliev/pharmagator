package com.eleks.academy.pharmagator.converters.request;

import com.eleks.academy.pharmagator.controllers.requests.PriceRequest;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.exceptions.NotNullConstraintViolationException;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class PriceRequestMapper implements RequestToEntityConverter<PriceRequest, Price> {

    @Override
    public Price toEntity(PriceRequest requestObject) {

        if(requestObject == null){
            throw new NotNullConstraintViolationException("PriceRequest can`t be null");
        }

        Price price = new Price();

        price.setPrice(requestObject.getPrice());

        price.setExternalId(requestObject.getExternalId());

        price.setUpdatedAt(Instant.now());

        return price;
    }
}
