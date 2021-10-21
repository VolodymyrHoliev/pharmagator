package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.controllers.requests.PriceRequest;
import com.eleks.academy.pharmagator.projections.PriceDto;
import com.eleks.academy.pharmagator.services.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public PriceDto getById(@PathVariable Long pharmacyId,
                            @PathVariable Long medicineId) {

        return priceService.findById(medicineId, pharmacyId);
    }

    @PostMapping
    public PriceDto create(@PathVariable Long medicineId,
                           @PathVariable Long pharmacyId,
                           @RequestBody PriceRequest priceRequest) {

        return priceService.save(priceRequest, medicineId, pharmacyId);
    }

    @DeleteMapping("/pharmacies/{pharmacyId}/medicines/{medicineId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long pharmacyId,
                                           @PathVariable Long medicineId) {

        priceService.delete(medicineId, pharmacyId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/pharmacies/{pharmacyId}/medicines/{medicineId}")
    public PriceDto update(@PathVariable Long pharmacyId,
                           @PathVariable Long medicineId,
                           @RequestBody PriceRequest priceRequest) {

        return priceService.update(medicineId, pharmacyId, priceRequest);
    }
}
