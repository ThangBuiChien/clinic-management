package com.example.clinic_management.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExaminationDetailUploadImgRequestDTO {

    private Long id;

    private String examinationType;

    private String examinationResult;

    private Long imagesCount;
}
