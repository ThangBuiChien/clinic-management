package com.example.clinic_management.service;

import java.util.List;

import com.example.clinic_management.dtos.requests.ExaminationRequestDTO;
import com.example.clinic_management.entities.ExaminationDetail;

public interface ExaminationDetailService {
    ExaminationDetail createExaminationDetail(ExaminationRequestDTO examinationRequestDTO);

    ExaminationDetail getExaminationDetailById(Long id);

    List<ExaminationDetail> getAllExaminationDetails();
}
