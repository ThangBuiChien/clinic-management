package com.example.clinic_management.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.example.clinic_management.dtos.requests.SymptomRequestDTO;
import com.example.clinic_management.dtos.responses.SymptomResponseDTO;
import com.example.clinic_management.entities.PrescribedDrug;
import com.example.clinic_management.entities.Symptom;

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
