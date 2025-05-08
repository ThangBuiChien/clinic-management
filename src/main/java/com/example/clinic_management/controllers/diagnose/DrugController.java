package com.example.clinic_management.controllers.diagnose;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.clinic_management.dtos.requests.DrugRequestDTO;
import com.example.clinic_management.dtos.responses.ApiResponse;
import com.example.clinic_management.service.diagnose.DrugService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/drug")
@RequiredArgsConstructor
public class DrugController {
    private final DrugService drugService;

    @GetMapping("")
    public ResponseEntity<ApiResponse> getAllDrugs() {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("All drugs fetched successfully")
                .result(drugService.getAllDrugs())
                .build());
    }

    @GetMapping("pageable")
    public ResponseEntity<ApiResponse> getAllDrugsPageable(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("All drugs pageable fetched successfully")
                .result(drugService.getAllDrugs(pageable))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getDrugById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Drug fetched successfully")
                .result(drugService.getDrugById(id))
                .build());
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse> addDrug(@Valid @RequestBody DrugRequestDTO drugRequestDTO) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Drug created successfully")
                .result(drugService.addDrug(drugRequestDTO))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateDrug(
            @PathVariable Long id, @Valid @RequestBody DrugRequestDTO drugRequestDTO) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Drug updated successfully")
                .result(drugService.updateDrug(id, drugRequestDTO))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteDrug(@PathVariable Long id) {
        drugService.deleteDrug(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Drug with id " + id + " deleted successfully")
                .build());
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchDrugs(@RequestParam String keyword, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Drugs searched successfully")
                .result(drugService.searchDrugs(keyword, pageable))
                .build());
    }
}
