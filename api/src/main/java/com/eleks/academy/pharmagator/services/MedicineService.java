package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.controllers.requests.MedicineRequest;
import com.eleks.academy.pharmagator.converters.request.RequestToEntityConverter;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.projections.MedicineDto;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicineService {

    private final MedicineRepository medicineRepository;

    private final RequestToEntityConverter<MedicineRequest, Medicine> mapper;

    private final ProjectionFactory projectionFactory;

    @Value("${pharmagator.error-messages.pharmacy-not-found-by-id}")
    private String errorMessage;


    public List<MedicineDto> findAll() {

        return medicineRepository.findAll(MedicineDto.class);
    }

    public MedicineDto save(@NotNull MedicineRequest medicineRequest) {

        Medicine medicine = mapper.toEntity(medicineRequest);

        medicineRepository.save(medicine);

        return projectionFactory.createProjection(MedicineDto.class, medicine);
    }

    public MedicineDto update(@NotNull Long id, @NotNull MedicineRequest medicineRequest) {

        Optional<MedicineDto> dtoOptional = medicineRepository.findById(id, MedicineDto.class);

        if (dtoOptional.isEmpty()) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
        } else {

            Medicine medicine = mapper.toEntity(medicineRequest);

            medicine.setId(id);

            medicineRepository.save(medicine);

            return projectionFactory.createProjection(MedicineDto.class, medicine);
        }
    }

    public MedicineDto findById(@NotNull Long id) {

        return medicineRepository.findById(id, MedicineDto.class)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage));
    }

    public void delete(@NotNull Long id) {

         medicineRepository.deleteById(id);
    }
}
