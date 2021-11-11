package com.eleks.academy.pharmagator.controllers;


import com.eleks.academy.pharmagator.controllers.requests.PriceRequest;
import com.eleks.academy.pharmagator.projections.PriceDto;
import com.eleks.academy.pharmagator.services.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/prices")
public class PriceController {

    private final PriceService priceService;

    @GetMapping
    public List<PriceDto> getAll() {

        return priceService.findAll();
    }

    @GetMapping("/pharmacies/{pharmacyId}/medicines/{medicineId}")
    public PriceDto getById(@PathVariable @Valid @Min(1) Long pharmacyId,
                            @PathVariable @Valid @Min(1) Long medicineId) {

        return priceService.findById(medicineId, pharmacyId);
    }

    @PostMapping("/pharmacies/{pharmacyId}/medicines/{medicineId}")
    public PriceDto create(@PathVariable @Valid @Min(1) Long medicineId,
                           @PathVariable @Valid @Min(1) Long pharmacyId,
                           @RequestBody PriceRequest priceRequest) {

        return priceService.save(priceRequest, medicineId, pharmacyId);
    }

    @DeleteMapping("/pharmacies/{pharmacyId}/medicines/{medicineId}")
    public ResponseEntity<Void> deleteById(@PathVariable @Valid @Min(1) Long pharmacyId,
                                           @PathVariable @Valid @Min(1) Long medicineId) {

        priceService.delete(medicineId, pharmacyId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/pharmacies/{pharmacyId}/medicines/{medicineId}")
    public PriceDto update(@PathVariable @Valid @Min(1) Long pharmacyId,
                           @PathVariable @Valid @Min(1) Long medicineId,
                           @RequestBody PriceRequest priceRequest) {

        return priceService.update(medicineId, pharmacyId, priceRequest);
    }

}
