package com.example.clinic_management.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ExaminationRequestDTO {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotBlank(message = "Examination type name is required")
    private String examinationType;

    @NotBlank(message = "Examination result name is required")
    @Size(max = 3070)
    private String examinationResult;

}
