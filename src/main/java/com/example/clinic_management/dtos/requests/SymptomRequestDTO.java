package com.example.clinic_management.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SymptomRequestDTO {
    @NotBlank(message = "Symptom name is required")
    @Size(max = 100)
    private String name;

    @NotNull(message = "Symptom description is required")
    @Size(max = 3070)
    private String description;
}
