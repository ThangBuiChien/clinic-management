package com.example.clinic_management.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.clinic_management.dtos.requests.PatientRequestDTO;
import com.example.clinic_management.dtos.responses.PatientResponseDTO;
import com.example.clinic_management.entities.Patient;
import com.example.clinic_management.enums.AccountStatus;
import com.example.clinic_management.enums.Role;
import com.example.clinic_management.exception.ResourceNotFoundException;
import com.example.clinic_management.mapper.AutoPatientMapper;
import com.example.clinic_management.repository.PatientRepository;
import com.example.clinic_management.service.user.PatientService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    private final AutoPatientMapper autoPatientMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public PatientResponseDTO addPatient(PatientRequestDTO patientRequestDTO) {
        Patient newPatient = patientRepository.save(autoPatientMapper.toEntity(patientRequestDTO));
        newPatient.setRole(Role.ROLE_PATIENT);
        newPatient.setStatus(AccountStatus.ACTIVE);

        newPatient.setPassword(passwordEncoder.encode(patientRequestDTO.getPassword()));

        return autoPatientMapper.toResponseDTO(patientRepository.save(newPatient));
    }

    @Override
    public PatientResponseDTO addNurse(PatientRequestDTO patientRequestDTO) {
        Patient newPatient = patientRepository.save(autoPatientMapper.toEntity(patientRequestDTO));
        newPatient.setRole(Role.ROLE_NURSE);
        newPatient.setStatus(AccountStatus.ACTIVE);

        newPatient.setPassword(passwordEncoder.encode(patientRequestDTO.getPassword()));

        return autoPatientMapper.toResponseDTO(patientRepository.save(newPatient));
    }

    @Override
    public PatientResponseDTO updatePatient(Long id, PatientRequestDTO patientRequestDTO) {
        Patient existingPatient =
                patientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));

        // Map the new values from patientRequestDTO to the existingPatient
        autoPatientMapper.updateFromDTO(patientRequestDTO, existingPatient);

        if (patientRequestDTO.getPassword() != null) {
            existingPatient.setPassword(passwordEncoder.encode(patientRequestDTO.getPassword()));
        }

        // Save the updated patient entity
        Patient updatedPatient = patientRepository.save(existingPatient);

        return autoPatientMapper.toResponseDTO(updatedPatient);
    }

    @Override
    public void deletePatient(Long id) {
        patientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));
        patientRepository.deleteById(id);
    }

    @Override
    public List<PatientResponseDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(autoPatientMapper::toResponseDTO)
                .toList();
    }

    @Override
    public Page<PatientResponseDTO> getAllPatients(Pageable pageable) {
        return patientRepository.findAll(pageable).map(autoPatientMapper::toResponseDTO);
    }

    @Override
    public PatientResponseDTO getPatientById(Long id) {
        return patientRepository
                .findById(id)
                .map(autoPatientMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));
    }
}
