package com.example.clinic_management.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.clinic_management.dtos.requests.DepartmentRequestDTO;
import com.example.clinic_management.dtos.responses.DepartmentResponseDTO;
import com.example.clinic_management.entities.Department;
import com.example.clinic_management.exception.ResourceNotFoundException;
import com.example.clinic_management.mapper.AutoDepartmentMapper;
import com.example.clinic_management.repository.DepartmentRepository;
import com.example.clinic_management.service.user.DepartmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final AutoDepartmentMapper autoDepartmentMapper;

    @Override
    public List<DepartmentResponseDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(autoDepartmentMapper::toResponseDTO)
                .toList();
    }

    @Override
    public Page<DepartmentResponseDTO> getAllDepartments(Pageable pageable) {
        Page<Department> departments = departmentRepository.findAll(pageable);
        List<DepartmentResponseDTO> departmentDTOs =
                departments.stream().map(autoDepartmentMapper::toResponseDTO).toList();
        return new PageImpl<>(departmentDTOs, pageable, departments.getTotalElements());
    }

    @Override
    public DepartmentResponseDTO getDepartmentById(Long id) {
        return departmentRepository
                .findById(id)
                .map(autoDepartmentMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
    }

    @Override
    public DepartmentResponseDTO addDepartment(DepartmentRequestDTO departmentRequestDTO) {
        Department newDepartment = autoDepartmentMapper.toEntity(departmentRequestDTO);
        departmentRepository.save(newDepartment);
        return autoDepartmentMapper.toResponseDTO(newDepartment);
    }

    @Override
    public DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO departmentResponseDTO) {
        Department oldDepartment = departmentRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        Department newDepartment = autoDepartmentMapper.toEntity(departmentResponseDTO);
        newDepartment.setId(oldDepartment.getId());
        newDepartment.setDoctors(oldDepartment.getDoctors());
        departmentRepository.save(newDepartment);
        return autoDepartmentMapper.toResponseDTO(newDepartment);
    }

    @Override
    public void deleteDepartment(Long id) {
        departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        departmentRepository.deleteById(id);
    }
}
