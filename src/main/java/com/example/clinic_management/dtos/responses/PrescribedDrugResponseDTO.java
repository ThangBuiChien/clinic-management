package com.example.clinic_management.dtos.responses;

import com.example.clinic_management.entities.PrescribedDrug;
import lombok.Data;

@Data
public class PrescribedDrugResponseDTO {
    private Long id;
    private String templateName;
    private Long drugId;
    private Integer dosage;
    private Integer duration;
    private String frequency;
    private String specialInstructions;


}