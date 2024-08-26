package com.example.clinic_management.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.clinic_management.dtos.requests.DepartmentRequestDTO;
import com.example.clinic_management.dtos.responses.DepartmentResponseDTO;

public interface DepartmentService {
    List<DepartmentResponseDTO> getAllDepartments();

    Page<DepartmentResponseDTO> getAllDepartments(Pageable pageable);

    DepartmentResponseDTO getDepartmentById(Long id);

    DepartmentResponseDTO addDepartment(DepartmentRequestDTO departmentResponseDTO);

    DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO departmentResponseDTO);

    void deleteDepartment(Long id);
}
