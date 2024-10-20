package com.example.clinic_management.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.clinic_management.dtos.requests.ExaminationRequestDTO;
import com.example.clinic_management.entities.ExaminationDetail;
import com.example.clinic_management.service.ExaminationDetailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/examination_detail")
@RequiredArgsConstructor
public class ExaminationDetailController {
    private final ExaminationDetailService examinationDetailService;

    @PostMapping("")
    public ResponseEntity<ExaminationDetail> createExaminationDetailService(
            @RequestBody ExaminationRequestDTO examinationRequestDTO) {
        ExaminationDetail createdExaminationDetail =
                examinationDetailService.createExaminationDetail(examinationRequestDTO);
        return ResponseEntity.ok(createdExaminationDetail);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExaminationDetail> getExaminationDetailById(@PathVariable Long id) {
        ExaminationDetail examinationDetail = examinationDetailService.getExaminationDetailById(id);
        return ResponseEntity.ok(examinationDetail);
    }

    @GetMapping("")
    public ResponseEntity<List<ExaminationDetail>> getAllExaminationDetails() {
        List<ExaminationDetail> examinationDetails = examinationDetailService.getAllExaminationDetails();
        return ResponseEntity.ok(examinationDetails);
    }
}
