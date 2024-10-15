package com.example.clinic_management.mapper;

import com.example.clinic_management.dtos.requests.PrescribedDrugRequestDTO;
import com.example.clinic_management.dtos.responses.PrescribedDrugResponseDTO;
import com.example.clinic_management.entities.PrescribedDrug;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AutoPrescribedDrugMapper {

    PrescribedDrug toEntity(PrescribedDrugRequestDTO prescribedDrugRequestDTO);

    @Mapping(source = "drug.id", target = "drugId") // Assuming drug ID needs to be mapped
    PrescribedDrugResponseDTO toResponseDTO(PrescribedDrug prescribedDrug);
}