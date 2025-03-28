package com.example.clinic_management.dtos.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SymptomResponseDTO {
    private Long id;
    private String name;
    private String description;
    private List<PrescribedDrugResponseDTO> prescribedDrugs;
}
