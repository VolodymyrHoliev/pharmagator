package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.entities.PriceId;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/prices")
public class PriceController {
    private final PriceRepository priceRepository;

    @GetMapping
    public ResponseEntity<List<Price>> getAll() {
        return ResponseEntity.ok(priceRepository.findAll());
    }

    @GetMapping("/{priceId}")
    public ResponseEntity<Price> getById(@PathVariable PriceId priceId) {
        return ResponseEntity.of(priceRepository.findById(priceId));
    }

    @PostMapping("/create")
    public ResponseEntity<Price> create(@RequestBody Price price) {
        return ResponseEntity.ok(priceRepository.save(price));
    }

    @DeleteMapping("/pharmacies/{pharmacyId}/medicines/{medicineId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long pharmacyId,@PathVariable Long medicineId) {
        PriceId priceId = new PriceId();
        priceId.setPharmacyId(pharmacyId);
        priceId.setMedicineId(medicineId);
        Optional<Price> optionalPrice = priceRepository.findById(priceId);
        if (optionalPrice.isPresent()) {
            priceRepository.deleteById(priceId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{priceId}")
    public ResponseEntity<Price> update(@PathVariable PriceId priceId, @RequestBody Price price) {
        Optional<Price> optionalById = priceRepository
                .findById(priceId);
        if (optionalById.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            long medicineId = priceId.getMedicineId();
            price.setMedicineId(medicineId);
            long pharmacyId = priceId.getPharmacyId();
            price.setPharmacyId(pharmacyId);
            priceRepository.save(price);
            return ResponseEntity.ok(price);
        }
    }
}
