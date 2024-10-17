package com.example.clinic_management.mapper;

import com.example.clinic_management.dtos.requests.PrescribedDrugRequestDTO;
import com.example.clinic_management.dtos.responses.PrescribedDrugResponseDTO;
import com.example.clinic_management.entities.PrescribedDrug;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AutoPrescribedDrugMapper {
    PrescribedDrugResponseDTO toResponseDTO(PrescribedDrug prescribedDrug);
    PrescribedDrug toEntity(PrescribedDrugRequestDTO prescribedDrugRequestDTO);
    List<PrescribedDrugResponseDTO> toResponseDTOs(List<PrescribedDrug> prescribedDrugs);
}