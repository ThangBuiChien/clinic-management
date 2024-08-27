package com.example.clinic_management.mapper;

import org.mapstruct.Mapper;

import com.example.clinic_management.dtos.requests.DrugRequestDTO;
import com.example.clinic_management.dtos.responses.DrugResponseDTO;
import com.example.clinic_management.entities.Drug;

@Mapper(componentModel = "spring")
public interface AutoDrugMapper {

    Drug toEntity(DrugRequestDTO drugRequestDTO);

    DrugResponseDTO toResponseDTO(Drug drug);
}
