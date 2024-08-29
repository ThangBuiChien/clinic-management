package com.example.clinic_management.mapper;

import com.example.clinic_management.dtos.requests.SymptomRequestDTO;
import com.example.clinic_management.dtos.responses.SymptomResponseDTO;
import com.example.clinic_management.entities.Symptom;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
    uses = {AutoSymptomMapper.class})
public interface AutoSymptomMapper {
    Symptom toEntity(SymptomRequestDTO symptomRequestDTO);
    SymptomResponseDTO toResponseDTO(Symptom symptom);
}
