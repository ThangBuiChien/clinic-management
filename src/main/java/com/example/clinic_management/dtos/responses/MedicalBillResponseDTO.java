package com.example.clinic_management.dtos.responses;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalBillResponseDTO {
    private Long id;
    private Long patientId;
    private String patientName;
    private String patientGender;
    private LocalDate patientBirthDate;
    private Long doctorId;
    private String doctorName;
    private LocalDate date;
    private String syndrome;
    private String note;
    private List<PrescribedDrugResponseDTO> prescribedDrugs;
    private List<ExaminationDetailResponseDTO> examinationDetails;
}
