package com.example.clinic_management.dtos.requests;

import java.time.LocalDate;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalBillPartialUpdateRequestDTO {

    @Size(max = 3070, message = "Syndrome description must be less than 3070 characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9\\s.,!?\"'()-]*$",
            message = "Syndrome description can only contain letters, numbers, space and basic punctuations")
    private String syndrome;

    private String note;

    private String finalDiagnosis;

    private LocalDate nextAppointmentDate;
}
