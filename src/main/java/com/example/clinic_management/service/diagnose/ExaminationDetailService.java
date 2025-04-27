package com.example.clinic_management.service.diagnose;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.clinic_management.dtos.requests.ExaminationDetailUploadImgRequestDTO;
import com.example.clinic_management.dtos.requests.ExaminationRequestDTO;
import com.example.clinic_management.dtos.responses.ExaminationDetailResponseDTO;
import com.example.clinic_management.entities.ExaminationDetail;
import com.example.clinic_management.enums.LabDepartment;
import com.example.clinic_management.enums.LabTest;

public interface ExaminationDetailService {
    ExaminationDetail createExaminationDetail(ExaminationRequestDTO examinationRequestDTO);

    ExaminationDetailResponseDTO getExaminationDetailById(Long id);

    List<ExaminationDetail> getAllExaminationDetails();

    List<ExaminationDetailResponseDTO> updateExaminationDetailWithImages(
            List<ExaminationDetailUploadImgRequestDTO> examinationDetailUploadImgRequestDTOs,
            List<MultipartFile> files);

    Set<LabTest> getLabTestsByDepartment(LabDepartment labDepartment);

    Page<ExaminationDetailResponseDTO> getExaminationDetailsByExaminationTypeAndImagesTestIsEmpty(
            LabTest examinationType, Pageable pageable);

    Page<ExaminationDetailResponseDTO> getExaminationDetailsByDepartmentAndImagesTestIsEmpty(
            LabDepartment labDepartment, Pageable pageable);

    Page<ExaminationDetailResponseDTO> getAllExaminationDetailsByImagesTestIsEmpty(Pageable pageable);

    Page<ExaminationDetailResponseDTO> getExaminationDetailsByDepartmentAndImagesTestIsEmptyAndCreatedAt(
            LabDepartment labDepartment, LocalDate createdAt, Pageable pageable);

    Page<ExaminationDetailResponseDTO> getExaminationDetailsByExaminationTypeAndImagesTestIsEmptyAndCreatedAt(
            LabTest examinationType, LocalDate createdAt, Pageable pageable);
}
