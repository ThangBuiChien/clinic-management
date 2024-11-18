package com.example.clinic_management.service;

import java.util.List;

import com.example.clinic_management.dtos.requests.ExaminationDetailUploadImgRequestDTO;
import com.example.clinic_management.dtos.requests.ExaminationRequestDTO;
import com.example.clinic_management.dtos.responses.ExaminationDetailResponseDTO;
import com.example.clinic_management.entities.ExaminationDetail;
import org.springframework.web.multipart.MultipartFile;

public interface ExaminationDetailService {
    ExaminationDetail createExaminationDetail(ExaminationRequestDTO examinationRequestDTO);

    ExaminationDetail getExaminationDetailById(Long id);

    List<ExaminationDetail> getAllExaminationDetails();

    List<ExaminationDetailResponseDTO> updateExaminationDetailWithImages(
            List<ExaminationDetailUploadImgRequestDTO> examinationDetailUploadImgRequestDTOs,
            List<MultipartFile> files);
}
