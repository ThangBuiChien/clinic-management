package com.example.clinic_management.controllers;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.clinic_management.dtos.requests.DepartmentRequestDTO;
import com.example.clinic_management.dtos.responses.ApiResponse;
import com.example.clinic_management.service.DepartmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/department")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping("")
    public ResponseEntity<ApiResponse> getAllDepartments() {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("All departments fetched successfully")
                .result(departmentService.getAllDepartments())
                .build());
    }

    @GetMapping("pageable")
    public ResponseEntity<ApiResponse> getAllDepartmentsPageable(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("All departments pageable fetched successfully")
                .result(departmentService.getAllDepartments(pageable))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Department fetched successfully")
                .result(departmentService.getDepartmentById(id))
                .build());
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse> addDepartment(@Valid @RequestBody DepartmentRequestDTO departmentRequestDTO) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Department created successfully")
                .result(departmentService.addDepartment(departmentRequestDTO))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateDepartment(
            @PathVariable Long id, @Valid @RequestBody DepartmentRequestDTO departmentRequestDTO) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Department updated successfully")
                .result(departmentService.updateDepartment(id, departmentRequestDTO))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Department with id " + id + " deleted successfully")
                .build());
    }
}
