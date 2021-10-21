package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.controllers.requests.PriceRequest;
import com.eleks.academy.pharmagator.converters.request.RequestToEntityConverter;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.projections.PriceDto;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceService {

    private final PriceRepository priceRepository;

    private final RequestToEntityConverter<PriceRequest, Price> mapper;

    private final ProjectionFactory projectionFactory;

    @Value("${pharmagator.error-messages.price-not-found-by-id}")
    private String errorMessage;

    public List<PriceDto> findAll() {

        return priceRepository.findAll(PriceDto.class);
    }

    public PriceDto findById(Long medicineId, Long pharmacyId) {

        return priceRepository.findByMedicineIdAndPharmacyId(medicineId, pharmacyId, PriceDto.class)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage));
    }

    public PriceDto save(PriceRequest priceRequest, Long medicineId, Long pharmacyId) {

        Price price = mapper.toEntity(priceRequest);

        price.setMedicineId(medicineId);

        price.setPharmacyId(pharmacyId);

        Price savedPrice = priceRepository.save(price);

        return projectionFactory.createProjection(PriceDto.class, savedPrice);
    }

    public PriceDto update(Long medicineId, Long pharmacyId,
                           PriceRequest priceRequest) {

        Optional<Price> entityOptional = priceRepository.
                findByMedicineIdAndPharmacyId(medicineId, pharmacyId, Price.class);

        if (entityOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
        } else {

            Price price = entityOptional.get();

            price.setPrice(priceRequest.getPrice());

            price.setExternalId(priceRequest.getExternalId());

            priceRepository.save(price);

            return projectionFactory.createProjection(PriceDto.class, price);
        }
    }

    public PriceDto delete(Long medicineId, Long pharmacyId) {

        Optional<PriceDto> priceDtoOptional = priceRepository
                .deleteByMedicineIdAndPharmacyId(medicineId, pharmacyId, PriceDto.class);

        return priceDtoOptional
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage));
    }
}
