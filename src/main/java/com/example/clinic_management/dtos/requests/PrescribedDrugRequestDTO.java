package com.example.clinic_management.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class PrescribedDrugRequestDTO {
    @NotNull(message = "Drug ID is required")
    private Long drugId;

    @NotNull(message = "Dosage is required")
    private int dosage;

    @NotNull(message = "Duration is required")
    private int duration;

    @NotNull(message = "Frequency is required")
    @Size(max = 50, message = "Frequency must be up to 50 characters")
    private String frequency;

    @Size(max = 200, message = "Special instructions must be up to 200 characters")
    private String specialInstructions;
}
