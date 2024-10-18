package com.example.clinic_management.controllers;

import com.example.clinic_management.dtos.requests.MedicalBillRequestDTO;
import com.example.clinic_management.dtos.responses.MedicalBillResponseDTO;
import com.example.clinic_management.service.MedicalBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical_bills")
@RequiredArgsConstructor
public class MedicalBillController {
    private final MedicalBillService medicalBillService;

    @PostMapping
    public ResponseEntity<MedicalBillResponseDTO> createMedicalBill(@RequestBody MedicalBillRequestDTO medicalBillRequestDTO) {
        MedicalBillResponseDTO responseDTO = medicalBillService.createMedicalBill(medicalBillRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalBillResponseDTO> getMedicalBill(@PathVariable Long id) {
        MedicalBillResponseDTO responseDTO = medicalBillService.getMedicalBillById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<MedicalBillResponseDTO>> getMedicalBills() {
        List<MedicalBillResponseDTO> responseDTOS = medicalBillService.getAllMedicalBill();
        return ResponseEntity.ok(responseDTOS);
    }
}
