package com.example.clinic_management.mapper;

import org.springframework.stereotype.Service;

import com.example.clinic_management.entities.Department;
import com.example.clinic_management.entities.Doctor;
import com.example.clinic_management.entities.Patient;
import com.example.clinic_management.exception.ResourceNotFoundException;
import com.example.clinic_management.repository.DepartmentRepository;
import com.example.clinic_management.repository.DoctorRepository;
import com.example.clinic_management.repository.PatientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MapperService {

    private final DepartmentRepository departmentRepository;

    private final DoctorRepository doctorRepository;

    private final PatientRepository patientRepository;

    public Department findDepartmentById(Long id) {
        return departmentRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
    }

    public Doctor findDoctorById(Long id) {
        return doctorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));
    }

    public Patient findPatientById(Long id) {
        return patientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));
    }
}
