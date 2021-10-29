package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.controllers.requests.PharmacyRequest;
import com.eleks.academy.pharmagator.converters.request.RequestToEntityConverter;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.exceptions.ObjectNotFoundException;
import com.eleks.academy.pharmagator.projections.PharmacyDto;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Validated
public class PharmacyService {

    private final PharmacyRepository pharmacyRepository;

    private final RequestToEntityConverter<PharmacyRequest, Pharmacy> mapper;

    private final ProjectionFactory projectionFactory;

    @Value("${pharmagator.error-messages.pharmacy-not-found-by-id}")
    private String errorMessage;

    public PharmacyDto save(@NotNull PharmacyRequest pharmacyRequest) {

        Pharmacy pharmacy = mapper.toEntity(pharmacyRequest);

        pharmacyRepository.save(pharmacy);

        return projectionFactory.createProjection(PharmacyDto.class, pharmacy);
    }

    public PharmacyDto update(@NotNull Long id, @Valid @NotNull PharmacyRequest pharmacyRequest) {

        Optional<Pharmacy> dtoOptional = pharmacyRepository.findById(id, Pharmacy.class);

        if (dtoOptional.isEmpty()) {

            throw new ObjectNotFoundException(errorMessage);

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
                .orElseThrow(() -> new ObjectNotFoundException(errorMessage));
    }

    public void delete(@NotNull Long pharmacyId) {

        this.findById(pharmacyId);

        pharmacyRepository.deleteById(pharmacyId);
    }
}
