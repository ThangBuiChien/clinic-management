package com.example.clinic_management.mapper;

import com.example.clinic_management.dtos.requests.PrescribedDrugRequestDTO;
import com.example.clinic_management.dtos.responses.PrescribedDrugResponseDTO;
import com.example.clinic_management.entities.PrescribedDrug;
import com.example.clinic_management.entities.Drug;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {Collectors.class, Drug.class})
public interface AutoPrescribedDrugMapper {

    @Mapping(target = "drugNames", expression = "java(mapDrugsToDrugNames(prescribedDrug.getDrugs()))")
    PrescribedDrugResponseDTO toResponseDTO(PrescribedDrug prescribedDrug);

    default List<String> mapDrugsToDrugNames(List<Drug> drugs) {
        return drugs.stream().map(Drug::getName).collect(Collectors.toList());
    }

    PrescribedDrug toEntity(PrescribedDrugRequestDTO prescribedDrugRequestDTO);

    List<PrescribedDrugResponseDTO> toResponseDTOs(List<PrescribedDrug> prescribedDrugs);
}