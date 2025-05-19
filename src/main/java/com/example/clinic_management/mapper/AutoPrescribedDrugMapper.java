package com.example.clinic_management.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.clinic_management.dtos.requests.PrescribedDrugRequestDTO;
import com.example.clinic_management.dtos.responses.PrescribedDrugResponseDTO;
import com.example.clinic_management.entities.PrescribedDrug;

@Mapper(
        componentModel = "spring",
        uses = {MapperService.class})
public interface AutoPrescribedDrugMapper {

    @Mapping(target = "drugName", source = "drug.name")
    @Mapping(target = "drugId", source = "drug.id")
    PrescribedDrugResponseDTO toResponseDTO(PrescribedDrug prescribedDrug);

    //    @Mapping(target = "drug", ignore = true)
    //    @Mapping(target = "id", ignore = true)
    @Mapping(target = "medicalBill", ignore = true)
    @Mapping(target = "drug", source = "drugId")
    PrescribedDrug toEntity(PrescribedDrugRequestDTO prescribedDrugRequestDTO);

    List<PrescribedDrugResponseDTO> toResponseDTOs(List<PrescribedDrug> prescribedDrugs);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "drug", ignore = true)
    @Mapping(target = "medicalBill", ignore = true)
    void updatePrescribedDrugFromDTO(
            PrescribedDrugRequestDTO prescribedDrugRequestDTO, @MappingTarget PrescribedDrug prescribedDrug);
}
