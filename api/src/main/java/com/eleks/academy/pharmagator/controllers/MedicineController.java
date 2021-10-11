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

    @GetMapping("/getById")
    public ResponseEntity<Medicine> getById(@RequestParam Long id) {
        return ResponseEntity.of(medicineRepository.findById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Medicine medicine) {
        return ResponseEntity.ok(medicineRepository.save(medicine));
    }


    @GetMapping("/deleteById")
    public ResponseEntity<?> deleteById(@RequestParam Long id) {
        Optional<Medicine> optionalMedicine = medicineRepository.findById(id);
        if (optionalMedicine.isPresent()) {
            medicineRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody Medicine medicine) {
        long id = medicine.getId();
        Optional<Medicine> optionalById = medicineRepository.findById(id);
        if (optionalById.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            String titleUpdate = medicine.getTitle();
            Medicine medicineById = optionalById.get();
            medicineById.setTitle(titleUpdate);
            medicineRepository.save(medicineById);
            return ResponseEntity.ok().build();
        }
    }

}
