package com.example.clinic_management.mapper;

import org.mapstruct.Mapper;

import com.example.clinic_management.dtos.requests.DepartmentRequestDTO;
import com.example.clinic_management.dtos.responses.DepartmentResponseDTO;
import com.example.clinic_management.entities.Department;

@Mapper(componentModel = "spring")
public interface AutoDepartmentMapper {

    Department toEntity(DepartmentRequestDTO departmentRequestDTO);

    DepartmentResponseDTO toResponseDTO(Department department);
}
