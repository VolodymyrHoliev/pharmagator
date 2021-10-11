package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.entities.Price;
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

    @GetMapping("/getById")
    public ResponseEntity<Price> getById(@RequestParam Long id) {
        return ResponseEntity.of(priceRepository.findById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Price price) {
        return ResponseEntity.ok(priceRepository.save(price));
    }

    @GetMapping("/deleteById")
    public ResponseEntity<?> deleteById(@RequestParam Long id) {
        Optional<Price> optionalPrice = priceRepository.findById(id);
        if (optionalPrice.isPresent()) {
            priceRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody Price price) {
        var medicineId = price.getMedicineId();
        var pharmacyId = price.getPharmacyId();
        Optional<Price> optionalById = priceRepository
                .findByMedicineIdAndPharmacyId(medicineId, pharmacyId);
        if (optionalById.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Price priceById = optionalById.get();
            BigDecimal priceUpdate = price.getPrice();
            String externalIdUpdate = price.getExternalId();
            priceById.setPrice(priceUpdate);
            priceById.setExternalId(externalIdUpdate);
            priceById.setUpdatedAt(Instant.now());
            priceRepository.save(priceById);
            return ResponseEntity.ok().build();
        }
    }
}
