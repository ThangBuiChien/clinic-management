package com.example.clinic_management.controllers.user;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.clinic_management.dtos.requests.PatientRequestDTO;
import com.example.clinic_management.dtos.responses.ApiResponse;
import com.example.clinic_management.service.user.PatientService;

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
                .message("Patient created successfully")
                .result(patientService.addPatient(patientRequestDTO))
                .build());
    }

    @PostMapping("/nurse")
    public ResponseEntity<ApiResponse> addNurse(@RequestBody @Valid PatientRequestDTO patientRequestDTO) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Nurse created successfully")
                .result(patientService.addNurse(patientRequestDTO))
                .build());
    }

    @PostMapping("/admin")
    public ResponseEntity<ApiResponse> addAdmin(@RequestBody @Valid PatientRequestDTO patientRequestDTO) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Admin created successfully")
                .result(patientService.addAdmin(patientRequestDTO))
                .build());
    }

    @PostMapping("/clinic-owner")
    public ResponseEntity<ApiResponse> addClinicOwner(@RequestBody @Valid PatientRequestDTO patientRequestDTO) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Clinic Owner created successfully")
                .result(patientService.addClinicOwner(patientRequestDTO))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updatePatient(
            @PathVariable Long id, @RequestBody @Valid PatientRequestDTO patientRequestDTO) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Patient updated successfully")
                .result(patientService.updatePatient(id, patientRequestDTO))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok(
                ApiResponse.builder().message("Patient deleted successfully").build());
    }
}
