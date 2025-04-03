package com.example.clinic_management.controllers.user;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.clinic_management.dtos.requests.DoctorPartialUpdateDTO;
import com.example.clinic_management.dtos.requests.DoctorRequestDTO;
import com.example.clinic_management.dtos.responses.ApiResponse;
import com.example.clinic_management.service.user.DoctorService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/doctor")
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping("")
    public ResponseEntity<ApiResponse> getAllDoctors(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("All doctors fetched successfully")
                .result(doctorService.getAllDoctors(pageable))
                .build());
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<ApiResponse> getDoctorsByDepartmentId(
            @ModelAttribute("pageable") Pageable pageable, @PathVariable Long departmentId) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Fetched doctor from department ID successfully")
                .result(doctorService.getDoctorsByDepartmentId(departmentId, pageable))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Doctor fetched by ID successfully")
                .result(doctorService.getDoctorById(id))
                .build());
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> getDoctorsByNameAndDepartment(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long departmentId,
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Fetched doctor Search successfully")
                .result(doctorService.getDoctorsByNameAndDepartment(name, departmentId, pageable))
                .build());
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse> addDoctor(@RequestBody @Valid DoctorRequestDTO doctorRequestDTO) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Doctor created successfully")
                .result(doctorService.addDoctor(doctorRequestDTO))
                .build());
    }

    @PostMapping("/nurse")
    public ResponseEntity<ApiResponse> addNurse(@RequestBody @Valid DoctorRequestDTO doctorRequestDTO) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Nurse created successfully")
                .result(doctorService.addNurse(doctorRequestDTO))
                .build());
    }



    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateDoctor(
            @PathVariable Long id, @RequestBody @Valid DoctorRequestDTO doctorRequestDTO) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Doctor updated successfully")
                .result(doctorService.updateDoctor(id, doctorRequestDTO))
                .build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> updatePartialDoctor(
            @PathVariable Long id, @RequestBody @Valid DoctorPartialUpdateDTO doctorPartialUpdateDTO) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Doctor updated partially successfully")
                .result(doctorService.updatePartialDoctor(id, doctorPartialUpdateDTO))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok(
                ApiResponse.builder().message("Doctor deleted successfully").build());
    }
}
