package com.example.clinic_management.service.diagnose;

import java.util.List;
import java.util.Set;

import com.example.clinic_management.enums.LabDepartment;
import com.example.clinic_management.enums.LabTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.clinic_management.dtos.requests.ExaminationDetailUploadImgRequestDTO;
import com.example.clinic_management.dtos.requests.ExaminationRequestDTO;
import com.example.clinic_management.dtos.responses.ExaminationDetailResponseDTO;
import com.example.clinic_management.entities.ExaminationDetail;

public interface ExaminationDetailService {
    ExaminationDetail createExaminationDetail(ExaminationRequestDTO examinationRequestDTO);

    ExaminationDetail getExaminationDetailById(Long id);

    List<ExaminationDetail> getAllExaminationDetails();

    List<ExaminationDetailResponseDTO> updateExaminationDetailWithImages(
            List<ExaminationDetailUploadImgRequestDTO> examinationDetailUploadImgRequestDTOs,
            List<MultipartFile> files);
    Set<LabTest> getLabTestsByDepartment(LabDepartment labDepartment);
}
