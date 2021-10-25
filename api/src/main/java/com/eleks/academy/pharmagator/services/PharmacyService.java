package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.controllers.requests.PharmacyRequest;
import com.eleks.academy.pharmagator.converters.request.RequestToEntityConverter;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.projections.PharmacyDto;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PharmacyService {

    private final PharmacyRepository pharmacyRepository;

    private final RequestToEntityConverter<PharmacyRequest, Pharmacy> mapper;

    private final ProjectionFactory projectionFactory;

    @Value("${pharmagator.error-messages.pharmacy-not-found-by-id}")
    private String errorMessage;

    public PharmacyDto save(@NotNull PharmacyRequest pharmacyRequest) {

        if(pharmacyRequest == null){
            throw new IllegalArgumentException("Pharmacy request can`t be null");
        }
        Pharmacy pharmacy = mapper.toEntity(pharmacyRequest);

        pharmacyRepository.save(pharmacy);

        return projectionFactory.createProjection(PharmacyDto.class, pharmacy);
    }

    public PharmacyDto update(@NotNull Long id, @NotNull PharmacyRequest pharmacyRequest) {

        Optional<Pharmacy> dtoOptional = pharmacyRepository.findById(id, Pharmacy.class);

        if (dtoOptional.isEmpty()) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);

        } else {

            Pharmacy pharmacy = mapper.toEntity(pharmacyRequest);

            pharmacy.setId(id);

            pharmacyRepository.save(pharmacy);

            return projectionFactory.createProjection(PharmacyDto.class, pharmacy);
        }
    }

    public List<PharmacyDto> findAll() {

        return pharmacyRepository.findAllPharmacies(PharmacyDto.class);
    }

    public PharmacyDto findById(@NotNull Long id) {

        return pharmacyRepository.findById(id, PharmacyDto.class)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage));
    }

    public void delete(@NotNull Long pharmacyId) {

        pharmacyRepository.deleteById(pharmacyId);
    }
}
