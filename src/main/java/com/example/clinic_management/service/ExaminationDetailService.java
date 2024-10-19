package com.example.clinic_management.service;

import com.example.clinic_management.dtos.requests.ExaminationRequestDTO;
import com.example.clinic_management.entities.ExaminationDetail;

import java.util.List;

public interface ExaminationDetailService {
    ExaminationDetail createExaminationDetail(ExaminationRequestDTO examinationRequestDTO);
    ExaminationDetail getExaminationDetailById(Long id);
    List<ExaminationDetail> getAllExaminationDetails();

}
