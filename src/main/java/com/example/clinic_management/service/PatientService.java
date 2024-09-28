package com.example.clinic_management.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.clinic_management.dtos.requests.PatientRequestDTO;
import com.example.clinic_management.dtos.responses.PatientResponseDTO;

public interface PatientService {
    PatientResponseDTO addPatient(PatientRequestDTO patientRequestDTO);

    PatientResponseDTO updatePatient(Long id, PatientRequestDTO patientRequestDTO);

    void deletePatient(Long id);

    List<PatientResponseDTO> getAllPatients();

    Page<PatientResponseDTO> getAllPatients(Pageable pageable);

    PatientResponseDTO getPatientById(Long id);
}
