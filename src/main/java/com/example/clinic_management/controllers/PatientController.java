package com.example.clinic_management.controllers;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.clinic_management.dtos.requests.PatientRequestDTO;
import com.example.clinic_management.dtos.responses.ApiResponse;
import com.example.clinic_management.service.PatientService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/patient")
public class PatientController {

    private final PatientService patientService;

    @GetMapping("")
    public ResponseEntity<ApiResponse> getAllPatients(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("All patients fetched successfully")
                .result(patientService.getAllPatients(pageable))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Patient fetched successfully")
                .result(patientService.getPatientById(id))
                .build());
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse> addPatient(@RequestBody @Valid PatientRequestDTO patientRequestDTO) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Doctor created successfully")
                .result(patientService.addPatient(patientRequestDTO))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updatePatient(
            @PathVariable Long id, @RequestBody @Valid PatientRequestDTO patientRequestDTO) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Doctor updated successfully")
                .result(patientService.updatePatient(id, patientRequestDTO))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok(
                ApiResponse.builder().message("Doctor deleted successfully").build());
    }
}
