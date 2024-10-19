package com.example.clinic_management.dtos.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MedicalBillResponseDTO {
    private Long id;
    private String patientName;
    private String patientDoB;
    private String patientGender;
    private String symptomName;
    private String syndrome;
//    private List<String> drugName;
    private String dosage;
    private String specialInstructions;
}
