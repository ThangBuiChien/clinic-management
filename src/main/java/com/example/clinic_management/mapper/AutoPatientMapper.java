package com.example.clinic_management.mapper;

import org.mapstruct.Mapper;

import com.example.clinic_management.dtos.requests.PatientRequestDTO;
import com.example.clinic_management.dtos.responses.PatientResponseDTO;
import com.example.clinic_management.entities.Patient;

@Mapper(componentModel = "spring")
public interface AutoPatientMapper {

    Patient toEntity(PatientRequestDTO patientRequestDTO);

    PatientResponseDTO toResponseDTO(Patient patient);
}
