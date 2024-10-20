package com.example.clinic_management.dtos.responses;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class PrescribedDrugResponseDTO {
    private Long id;
    private String drugName;
    private int dosage;
    private int duration;
    private String frequency;
    private String specialInstructions;
}