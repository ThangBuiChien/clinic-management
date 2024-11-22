package com.example.clinic_management.dtos.requests;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalBillWithLabRequestDTO {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotBlank(message = "Syndrome is required")
    private String syndrome;

    private String note;

    private List<ExaminationDetailLabRequestDTO> examinationDetailLabRequestDTOS;
}
