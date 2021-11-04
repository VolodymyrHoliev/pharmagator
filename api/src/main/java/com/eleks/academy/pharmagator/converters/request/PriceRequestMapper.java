package com.eleks.academy.pharmagator.converters.request;

import com.eleks.academy.pharmagator.controllers.requests.PriceRequest;
import com.eleks.academy.pharmagator.entities.Price;
import org.springframework.stereotype.Component;

@Component
public class PriceRequestMapper implements RequestToEntityConverter<PriceRequest, Price> {

    @Override
    public Price toEntity(PriceRequest requestObject) {

        Price price = new Price();

        price.setPrice(requestObject.getPrice());

        price.setExternalId(price.getExternalId());

        return price;
    }
}
