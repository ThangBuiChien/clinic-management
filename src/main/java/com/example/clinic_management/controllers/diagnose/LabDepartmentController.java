package com.example.clinic_management.controllers.diagnose;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.clinic_management.dtos.responses.ExaminationDetailResponseDTO;
import com.example.clinic_management.enums.LabDepartment;
import com.example.clinic_management.enums.LabTest;
import com.example.clinic_management.service.diagnose.ExaminationDetailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/lab_department")
public class LabDepartmentController {

    private final ExaminationDetailService examinationDetailService;

    @GetMapping("")
    public ResponseEntity<LabDepartment[]> getAllLabDepartments() {
        LabDepartment[] labDepartments = LabDepartment.values();
        return ResponseEntity.ok(labDepartments);
    }

    @GetMapping("/lab_tests")
    public ResponseEntity<Set<LabTest>> getLabTestsByDepartment(@RequestParam LabDepartment labDepartment) {
        Set<LabTest> labTests = examinationDetailService.getLabTestsByDepartment(labDepartment);
        return ResponseEntity.ok(labTests);
    }

    @GetMapping("by_department_and_created_at")
    public ResponseEntity<Page<ExaminationDetailResponseDTO>> getExaminationDetails(
            @RequestParam LabDepartment labDepartment, @RequestParam LocalDate createdAt, Pageable pageable) {
        Page<ExaminationDetailResponseDTO> page =
                examinationDetailService.getExaminationDetailsByDepartmentAndImagesTestIsEmptyAndCreatedAt(
                        labDepartment, createdAt, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("by_lab_type_and_created_at")
    public ResponseEntity<Page<ExaminationDetailResponseDTO>> getExaminationDetails(
            @RequestParam LabTest labTest, @RequestParam LocalDate createdAt, Pageable pageable) {
        Page<ExaminationDetailResponseDTO> page =
                examinationDetailService.getExaminationDetailsByExaminationTypeAndImagesTestIsEmptyAndCreatedAt(
                        labTest, createdAt, pageable);
        return ResponseEntity.ok(page);
    }
}
