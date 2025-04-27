package com.example.clinic_management.mapper;

import com.example.clinic_management.dtos.requests.DoctorRequestDTO;
import com.example.clinic_management.entities.Doctor;
import com.example.clinic_management.entities.PrescribedDrug;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;

import com.example.clinic_management.dtos.requests.SymptomRequestDTO;
import com.example.clinic_management.dtos.responses.SymptomResponseDTO;
import com.example.clinic_management.entities.Symptom;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = {AutoPrescribedDrugMapper.class})
public interface AutoSymptomMapper {
    Symptom toEntity(SymptomRequestDTO symptomRequestDTO);

    SymptomResponseDTO toResponseDTO(Symptom symptom);

    // Update from symptomRequestDTO
    void updateFromDTO(SymptomRequestDTO symptomRequestDTO, @MappingTarget Symptom symptom);

    @AfterMapping
    default void setSymptomReference(@MappingTarget Symptom symptom) {
        if (symptom.getPrescribedDrugs() != null) {
            for (PrescribedDrug prescribedDrug : symptom.getPrescribedDrugs()) {
                prescribedDrug.setSymptom(symptom);
            }
        }
    }

}
