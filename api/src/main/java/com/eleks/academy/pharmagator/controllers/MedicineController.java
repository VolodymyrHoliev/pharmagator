package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.controllers.requests.MedicineRequest;
import com.eleks.academy.pharmagator.projections.MedicineDto;
import com.eleks.academy.pharmagator.services.MedicineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
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
    public MedicineDto create(@RequestBody MedicineRequest medicineRequest) {

        return medicineService.save(medicineRequest);
    }


    @DeleteMapping("/{medicineId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long medicineId) {

        medicineService.delete(medicineId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{medicineId}")
    public MedicineDto update(@PathVariable Long medicineId, @RequestBody MedicineRequest medicineRequest) {

        return medicineService.update(medicineId, medicineRequest);
    }

}
