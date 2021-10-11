package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/pharmacies")
public class PharmacyController {
    private final PharmacyRepository pharmacyRepository;

    @GetMapping
    public ResponseEntity<List<Pharmacy>> getAll() {
        return ResponseEntity.ok(pharmacyRepository.findAll());
    }

    @GetMapping("/getById")
    public ResponseEntity<Pharmacy> getById(@RequestParam Long id) {
        return ResponseEntity.of(pharmacyRepository.findById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Pharmacy pharmacy) {
        return ResponseEntity.ok(pharmacyRepository.save(pharmacy));
    }


    @GetMapping("/deleteById")
    public ResponseEntity<?> deleteById(@RequestParam Long id) {
        Optional<Pharmacy> optionalPharmacy = pharmacyRepository.findById(id);
        if (optionalPharmacy.isPresent()) {
            pharmacyRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody Pharmacy pharmacy) {
        long id = pharmacy.getId();
        Optional<Pharmacy> optionalById = pharmacyRepository.findById(id);
        if (optionalById.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Pharmacy pharmacyById = optionalById.get();
            String updateForName = pharmacy.getName();
            pharmacyById.setName(updateForName);
            String updateForMedicineLinkTemplate = pharmacy.getMedicineLinkTemplate();
            pharmacyById.setMedicineLinkTemplate(updateForMedicineLinkTemplate);
            pharmacyRepository.save(pharmacyById);
            return ResponseEntity.ok().build();
        }
    }
}
