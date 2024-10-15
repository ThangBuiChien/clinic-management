package com.example.clinic_management.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PrescribedDrugRequestDTO {
    @NotNull(message = "Drug ID is required")
    @Positive(message = "Drug ID must be a positive number")
    private Long drugId;

    @NotNull(message = "Template Name is required")
    @Size(min = 1, max = 50, message = "Template Name must be between 1 and 50 characters")
    private String templateName;

    @NotNull(message = "Dosage is required")
    @Positive(message = "Dosage must be a positive number")
    private Integer dosage;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be a positive number")
    private Integer duration;

    @NotNull(message = "Frequency is required")
    @Size(min = 1, max = 50, message = "Frequency must be between 1 and 50 characters")
    private String frequency;

    @Size(max = 200, message = "Special instructions must be up to 200 characters")
    private String specialInstructions;
}