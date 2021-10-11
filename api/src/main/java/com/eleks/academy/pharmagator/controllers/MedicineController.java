package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/medicines")
public class MedicineController {
    private final MedicineRepository medicineRepository;

    @GetMapping
    public ResponseEntity<List<Medicine>> getAll() {
        return ResponseEntity.ok(medicineRepository.findAll());
    }

    @GetMapping("/{medicineId}")
    public ResponseEntity<Medicine> getById(@PathVariable Long medicineId) {
        return ResponseEntity.of(medicineRepository.findById(medicineId));
    }

    @PostMapping("/")
    public ResponseEntity<Medicine> create(@RequestBody Medicine medicine) {
        return ResponseEntity.ok(medicineRepository.save(medicine));
    }


    @DeleteMapping("/{medicineId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long medicineId) {
        Optional<Medicine> optionalMedicine = medicineRepository.findById(medicineId);
        if (optionalMedicine.isPresent()) {
            medicineRepository.deleteById(medicineId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{medicineId}")
    public ResponseEntity<Medicine> update(@PathVariable Long medicineId, @RequestBody Medicine medicine) {
        Optional<Medicine> optionalById = medicineRepository.findById(medicineId);
        if (optionalById.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            medicine.setId(medicineId);
            medicineRepository.save(medicine);
            return ResponseEntity.ok(medicine);
        }
    }

}
