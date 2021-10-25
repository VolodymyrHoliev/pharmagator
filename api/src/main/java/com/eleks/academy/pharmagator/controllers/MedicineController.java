package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.controllers.requests.MedicineRequest;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.projections.MedicineDto;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import com.eleks.academy.pharmagator.services.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/medicines")
public class MedicineController {

    private final MedicineService medicineService;

    @GetMapping
    public List<MedicineDto> getAll() {

        return medicineService.findAll();
    }

    @GetMapping("/{medicineId}")
    public MedicineDto getById(@PathVariable Long medicineId) {

        return medicineService.findById(medicineId);
    }

    @PostMapping("/")
    public MedicineDto create(@Valid @RequestBody MedicineRequest medicineRequest) {

        return medicineService.save(medicineRequest);
    }


    @DeleteMapping("/{medicineId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long medicineId) {

        medicineService.delete(medicineId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{medicineId}")
    public MedicineDto update(@PathVariable Long medicineId,
                              @Valid @RequestBody MedicineRequest medicineRequest) {

        return medicineService.update(medicineId, medicineRequest);
    }
}
