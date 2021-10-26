package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.controllers.requests.PriceRequest;
import com.eleks.academy.pharmagator.converters.request.RequestToEntityConverter;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.projections.PriceDto;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceService {

    private final PriceRepository priceRepository;

    private final PharmacyRepository pharmacyRepository;

    private final MedicineRepository medicineRepository;

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

        if (priceRequest == null) {

            throw new IllegalArgumentException("PriceRequest can`t be null");
        }

        medicineRepository.findById(medicineId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Can`t save price due to: " +
                                "Medicine with specified id not found"));
        pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Can`t save price due to: " +
                                "Pharmacy with specified id not found"));

        Price price = mapper.toEntity(priceRequest);

        price.setMedicineId(medicineId);

        price.setPharmacyId(pharmacyId);

        Optional<PriceDto> priceDtoOptional = priceRepository
                .findByMedicineIdAndPharmacyId(medicineId, pharmacyId, PriceDto.class);

        if(priceDtoOptional.isPresent()){

            throw new IllegalArgumentException("Price with specified pharmacyId and medicineId " +
                    "already exists.Consider use put request if you need to update existing price");

        }else {

            Price savedPrice = priceRepository.save(price);

            return projectionFactory.createProjection(PriceDto.class, savedPrice);
        }
    }

    public PriceDto update(Long medicineId, Long pharmacyId,
                           PriceRequest priceRequest) {

        Optional<Price> entityOptional = priceRepository.
                findByMedicineIdAndPharmacyId(medicineId, pharmacyId, Price.class);

        if (entityOptional.isEmpty()) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
        } else if (priceRequest == null) {

            throw new IllegalArgumentException("PriceRequest can`t be null");
        } else {

            Price price = entityOptional.get();

            price.setPrice(priceRequest.getPrice());

            price.setExternalId(priceRequest.getExternalId());

            priceRepository.save(price);

            return projectionFactory.createProjection(PriceDto.class, price);
        }
    }

    public void delete(Long medicineId, Long pharmacyId) {
        priceRepository.findByMedicineIdAndPharmacyId(medicineId, pharmacyId, Price.class)
                        .orElseThrow(() ->
                                new IllegalArgumentException("Attempt to delete not existing entity.Check " +
                                        "'medicineId' and 'pharmacyId' values"));
        priceRepository
                .deleteByMedicineIdAndPharmacyId(medicineId, pharmacyId);
    }
}
