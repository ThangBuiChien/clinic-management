package com.example.clinic_management.controllers;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.clinic_management.dtos.requests.SymptomRequestDTO;
import com.example.clinic_management.dtos.responses.ApiResponse;
import com.example.clinic_management.service.SymptomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/symptom")
@RequiredArgsConstructor
public class SymptomController {
    private final SymptomService symptomService;

    @GetMapping()
    public ResponseEntity<ApiResponse> getAllSymptoms() {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("All symptoms fetched successfully")
                .result(symptomService.getAllSymptoms())
                .build());
    }

    @GetMapping("/pageable")
    public ResponseEntity<ApiResponse> getAllSymptoms(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("All symptoms pageable fetched successfully")
                .result(symptomService.getAllSymptoms(pageable))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getSymptomById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Symptom fetched successfully")
                .result(symptomService.getSymptomById(id))
                .build());
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse> addSymptom(@Valid @RequestBody SymptomRequestDTO symptomRequestDTO) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Symptom created successfully")
                .result(symptomService.addSymptom(symptomRequestDTO))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateSymptom(
            @PathVariable Long id, @Valid @RequestBody SymptomRequestDTO symptomRequestDTO) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Symptom updated successfully")
                .result(symptomService.updateSymptom(id, symptomRequestDTO))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteSymptom(@PathVariable Long id) {
        symptomService.deleteSymptom(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Symptom with id " + id + " deleted successfully")
                .build());
    }
}
