package com.example.clinic_management.dtos.responses;

import lombok.Data;

import java.util.List;

@Data
public class PrescribedDrugResponseDTO {
    private Long id;
    private String symtomName;
    private List<String> drugNames;
    private String dosage;
    private String specialInstructions;
}
