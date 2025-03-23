package com.example.clinic_management.controllers.diagnose;

import com.example.clinic_management.enums.LabDepartment;
import com.example.clinic_management.enums.LabTest;
import com.example.clinic_management.service.diagnose.ExaminationDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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


}
