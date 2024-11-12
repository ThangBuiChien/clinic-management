package com.example.clinic_management.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExaminationDetailRequestDTO {

    //    private String patientName;
    //
    //    private String doctorName;

    private String examinationType;

    private String examinationResult;

    private Long imagesCount;
}
