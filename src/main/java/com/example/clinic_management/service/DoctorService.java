package com.example.clinic_management.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.clinic_management.dtos.requests.DoctorRequestDTO;
import com.example.clinic_management.dtos.responses.DoctorResponseDTO;

public interface DoctorService {

    Page<DoctorResponseDTO> getAllDoctors(Pageable pageable);

    DoctorResponseDTO getDoctorById(Long id);

    Page<DoctorResponseDTO> getDoctorsByDepartmentId(Long departmentId, Pageable pageable);

    DoctorResponseDTO addDoctor(DoctorRequestDTO doctorRequestDTO);

    DoctorResponseDTO updateDoctor(Long id, DoctorRequestDTO doctorRequestDTO);

    void deleteDoctor(Long id);
}
