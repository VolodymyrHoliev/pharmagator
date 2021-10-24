package com.eleks.academy.pharmagator.controllers;
import com.eleks.academy.pharmagator.controllers.requests.PharmacyRequest;
import com.eleks.academy.pharmagator.projections.PharmacyDto;
import com.eleks.academy.pharmagator.services.PharmacyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
    public PharmacyDto getById(@PathVariable Long pharmacyId) {

        return pharmacyService.findById(pharmacyId);
    }

    @PostMapping("/")
    public PharmacyDto create(@Valid @RequestBody PharmacyRequest requestBody) {

        return pharmacyService.save(requestBody);
    }


    @DeleteMapping("/{pharmacyId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long pharmacyId) {

        pharmacyService.delete(pharmacyId);

        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{pharmacyId}")
    public PharmacyDto update(@PathVariable Long pharmacyId,
                              @RequestBody PharmacyRequest requestBody) {

        return pharmacyService.update(pharmacyId, requestBody);

    }
}