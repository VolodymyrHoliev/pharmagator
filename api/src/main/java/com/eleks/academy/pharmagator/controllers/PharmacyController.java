package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.controllers.requests.PharmacyRequest;
import com.eleks.academy.pharmagator.projections.PharmacyDto;
import com.eleks.academy.pharmagator.services.PharmacyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pharmacies")
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @GetMapping
    public List<PharmacyDto> getAll() {

        return pharmacyService.findAll();
    }

    @GetMapping("/{pharmacyId}")
    public PharmacyDto getById(@PathVariable @Positive Long pharmacyId) {

        return pharmacyService.findById(pharmacyId);
    }

    @PostMapping("/")
    public PharmacyDto create(@Valid @RequestBody PharmacyRequest requestBody) {

        return pharmacyService.save(requestBody);
    }


    @DeleteMapping("/{pharmacyId}")
    public ResponseEntity<Void> deleteById(@PathVariable @Positive Long pharmacyId) {

        pharmacyService.delete(pharmacyId);

        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{pharmacyId}")
    public PharmacyDto update(@PathVariable @Positive Long pharmacyId,
                              @Valid @RequestBody PharmacyRequest requestBody) {

        return pharmacyService.update(pharmacyId, requestBody);

    }
}