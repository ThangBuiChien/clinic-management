package com.example.clinic_management.mapper;

import org.springframework.stereotype.Service;

import com.example.clinic_management.entities.Department;
import com.example.clinic_management.exception.ResourceNotFoundException;
import com.example.clinic_management.repository.DepartmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MapperService {

    private final DepartmentRepository departmentRepository;

    public Department findDepartmentById(Long id) {
        return departmentRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
    }
}
