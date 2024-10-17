package com.example.clinic_management.dtos.responses;

import com.example.clinic_management.entities.Drug;
import lombok.Data;

import java.util.List;

@Data
public class PrescribedDrugResponseDTO {
    private Long id; // ID of the PrescribedDrug
    private String symtomName;
    private List<Drug> drugs; // List of drugs associated with this prescription
    private String dosage;
    private String specialInstructions;
}