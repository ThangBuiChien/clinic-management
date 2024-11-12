package com.example.clinic_management.controllers;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.example.clinic_management.dtos.requests.MedicalBillRequestDTO;
import com.example.clinic_management.dtos.responses.MedicalBillResponseDTO;
import com.example.clinic_management.service.MedicalBillService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/medical-bills")
@RequiredArgsConstructor
public class MedicalBillController {
    private final MedicalBillService medicalBillService;

    @PostMapping
    public ResponseEntity<MedicalBillResponseDTO> createMedicalBill(
            @Valid @RequestBody MedicalBillRequestDTO medicalBillRequestDTO) {
        MedicalBillResponseDTO responseDTO = medicalBillService.createMedicalBill(medicalBillRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping(value = "images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MedicalBillResponseDTO> createMedicalBillWithImages(
            @RequestPart("medicalBillData") MedicalBillRequestDTO medicalBillRequestDTO,
            @RequestPart("files") List<MultipartFile> files) {
        MedicalBillResponseDTO responseDTO = medicalBillService.createMedicalBillWithImage(medicalBillRequestDTO,files);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<MedicalBillResponseDTO>> getMedicalBills(Pageable pageable) {
        Page<MedicalBillResponseDTO> responseDTOS = medicalBillService.getAllMedicalBills(pageable);
        return ResponseEntity.ok(responseDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalBillResponseDTO> getMedicalBillById(@PathVariable Long id) {
        MedicalBillResponseDTO responseDTO = medicalBillService.getMedicalBillById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalBillResponseDTO> updateMedicalBill(
            @PathVariable Long id, @Valid @RequestBody MedicalBillRequestDTO medicalBillRequestDTO) {
        MedicalBillResponseDTO responseDTO = medicalBillService.updateMedicalBill(id, medicalBillRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalBill(@PathVariable Long id) {
        medicalBillService.deleteMedicalBill(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/{patientName}")
    public ResponseEntity<List<MedicalBillResponseDTO>> getMedicalBillsByPatientName(@PathVariable String patientName) {
        List<MedicalBillResponseDTO> responseDTOS = medicalBillService.findMedicalBillsByPatientName(patientName);
        return ResponseEntity.ok(responseDTOS);
    }

    @GetMapping("/doctor/{doctorName}")
    public ResponseEntity<List<MedicalBillResponseDTO>> getMedicalBillsByDoctorName(@PathVariable String doctorName) {
        List<MedicalBillResponseDTO> responseDTOS = medicalBillService.findMedicalBillsByDoctorName(doctorName);
        return ResponseEntity.ok(responseDTOS);
    }
}
