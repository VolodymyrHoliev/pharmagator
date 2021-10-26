package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.controllers.requests.PriceRequest;
import com.eleks.academy.pharmagator.projections.PriceDto;
import com.eleks.academy.pharmagator.services.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/prices")
@Validated
public class PriceController {

    private final PriceService priceService;

    @GetMapping
    public List<PriceDto> getAll() {

        return priceService.findAll();
    }

    @GetMapping("/pharmacies/{pharmacyId}/medicines/{medicineId}")
    public PriceDto getById(@PathVariable @Min(value = 1) Long pharmacyId,
                            @PathVariable @Min(1) Long medicineId) {

        return priceService.findById(medicineId, pharmacyId);
    }

    @PostMapping("/pharmacies/{pharmacyId}/medicines/{medicineId}")
    public PriceDto create(@PathVariable @Min(1) Long medicineId,
                           @PathVariable @Min(1) Long pharmacyId,
                           @Valid @RequestBody PriceRequest priceRequest) {

        return priceService.save(priceRequest, medicineId, pharmacyId);
    }

    @DeleteMapping("/pharmacies/{pharmacyId}/medicines/{medicineId}")
    public ResponseEntity<Void> deleteById(@PathVariable @Min(1) Long pharmacyId,
                                           @PathVariable @Min(1) Long medicineId) {

        priceService.delete(medicineId, pharmacyId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/pharmacies/{pharmacyId}/medicines/{medicineId}")
    public PriceDto update(@PathVariable @Min(1) Long pharmacyId,
                           @PathVariable @Min(1) Long medicineId,
                           @Valid @RequestBody PriceRequest priceRequest) {

        return priceService.update(medicineId, pharmacyId, priceRequest);
    }
}
