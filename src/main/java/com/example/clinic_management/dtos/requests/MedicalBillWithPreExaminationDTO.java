package com.example.clinic_management.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MedicalBillWithPreExaminationDTO {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotBlank(message = "Syndrome is required")
    private String syndrome;

    private String note;

    private Double weight;
    private Integer heartRate;
    private String bloodPressure;
    private Double temperature;


}
