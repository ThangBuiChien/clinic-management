package com.example.clinic_management.dtos.responses;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SymptomResponseDTO {
    private Long id;
    private String name;
    private String description;
    private List<PrescribedDrugResponseDTO> prescribedDrugs;
}
