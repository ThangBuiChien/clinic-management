package com.example.clinic_management.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class PrescribedDrugRequestDTO {
    @NotNull(message = "At least one Drug ID is required")
    private List<Long> drugIds; // List of existing drug IDs

    @NotNull(message = "Symptom Name is required")
    @Size(min = 1, max = 50, message = "Template Name must be between 1 and 50 characters")
    private String symptomName;

    @NotNull(message = "Dosage is required")
    @Size(max = 50, message = "Dosage must be up to 50 characters") // Adjusting validation
    private String dosage; 

    @Size(max = 200, message = "Special instructions must be up to 200 characters")
    private String specialInstructions;
}
