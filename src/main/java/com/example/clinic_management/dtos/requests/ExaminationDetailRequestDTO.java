package com.example.clinic_management.dtos.requests;

import com.example.clinic_management.enums.LabTest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExaminationDetailRequestDTO {

    //    private String patientName;
    //
    //    private String doctorName;

    private LabTest examinationType;

    private String examinationResult;

    private Long imagesCount;
}
