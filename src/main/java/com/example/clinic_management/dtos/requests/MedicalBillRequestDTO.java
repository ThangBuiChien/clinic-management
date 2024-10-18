package com.example.clinic_management.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class MedicalBillRequestDTO {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotBlank(message = "Symptom name is required")
    private String symptomName;

    // Optional fields for new prescription
    private String syndrome;
    private List<String> drugName;
    private String dosage;
    private String specialInstructions;
}
