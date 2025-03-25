package com.example.clinic_management.dtos.requests;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class MedicalBillRequestDTO {
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

    private String finalDiagnosis;

    //    private List<Long> prescribedDrugIds;
    private List<PrescribedDrugRequestDTO> prescribedDrugRequestDTOS;

    private List<ExaminationDetailRequestDTO> examinationDetailRequestDTOS;
}
