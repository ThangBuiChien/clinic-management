package com.example.clinic_management.controllers;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.clinic_management.dtos.requests.PrescribedDrugRequestDTO;
import com.example.clinic_management.dtos.responses.ApiResponse;
import com.example.clinic_management.dtos.responses.PrescribedDrugResponseDTO;
import com.example.clinic_management.service.PrescribedDrugService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/prescribed-drugs")
@RequiredArgsConstructor
public class PrescribedDrugController {

    private final PrescribedDrugService prescribedDrugService;

    @GetMapping("")
    public ResponseEntity<ApiResponse> getAllPrescribedDrugs() {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("All prescribed drugs fetched successfully")
                .result(prescribedDrugService.getAllPrescribedDrugs())
                .build());
    }

    @GetMapping("pageable")
    public ResponseEntity<ApiResponse> getAllPrescribedDrugsPageable(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("All prescribed drugs pageable fetched successfully")
                .result(prescribedDrugService.getAllPrescribedDrugs(pageable))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getPrescribedDrugById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Prescribed drug fetched successfully")
                .result(prescribedDrugService.getPrescribedDrugById(id))
                .build());
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse> addPrescribedDrug(
            @Valid @RequestBody PrescribedDrugRequestDTO prescribedDrugRequestDTO) {
        PrescribedDrugResponseDTO createdPrescribedDrug =
                prescribedDrugService.addPrescribedDrug(prescribedDrugRequestDTO);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Prescribed drug created successfully")
                .result(createdPrescribedDrug)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updatePrescribedDrug(
            @PathVariable Long id, @Valid @RequestBody PrescribedDrugRequestDTO prescribedDrugRequestDTO) {
        PrescribedDrugResponseDTO updatedPrescribedDrug =
                prescribedDrugService.updatePrescribedDrug(id, prescribedDrugRequestDTO);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Prescribed drug updated successfully")
                .result(updatedPrescribedDrug)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePrescribedDrug(@PathVariable Long id) {
        prescribedDrugService.deletePrescribedDrug(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Prescribed drug with id " + id + " deleted successfully")
                .build());
    }
}
