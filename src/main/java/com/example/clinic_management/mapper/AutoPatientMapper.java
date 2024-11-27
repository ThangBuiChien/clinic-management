package com.example.clinic_management.mapper;

import org.mapstruct.Mapper;

import com.example.clinic_management.dtos.requests.PatientRequestDTO;
import com.example.clinic_management.dtos.responses.PatientResponseDTO;
import com.example.clinic_management.entities.Patient;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AutoPatientMapper {

    Patient toEntity(PatientRequestDTO patientRequestDTO);

    PatientResponseDTO toResponseDTO(Patient patient);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    void updateFromDTO(PatientRequestDTO patientRequestDTO, @MappingTarget Patient patient);
}
