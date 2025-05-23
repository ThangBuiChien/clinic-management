package com.example.clinic_management.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.clinic_management.dtos.requests.*;
import com.example.clinic_management.dtos.responses.MedicalBillResponseDTO;
import com.example.clinic_management.entities.ExaminationDetail;
import com.example.clinic_management.entities.Image;
import com.example.clinic_management.entities.MedicalBill;
import com.example.clinic_management.entities.PrescribedDrug;
import com.example.clinic_management.enums.ExaminationDetailStatus;
import com.example.clinic_management.exception.ResourceNotFoundException;
import com.example.clinic_management.mapper.AutoExaminationDetailMapper;
import com.example.clinic_management.mapper.AutoMedicalBillMapper;
import com.example.clinic_management.mapper.AutoPrescribedDrugMapper;
import com.example.clinic_management.repository.DoctorRepository;
import com.example.clinic_management.repository.MedicalBillRepository;
import com.example.clinic_management.repository.PatientRepository;
import com.example.clinic_management.repository.PrescribedDrugRepository;
import com.example.clinic_management.service.diagnose.ImageService;
import com.example.clinic_management.service.diagnose.MedicalBillService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicalBillServiceImpl implements MedicalBillService {
    private final MedicalBillRepository medicalBillRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PrescribedDrugRepository prescribedDrugRepository;
    private final AutoMedicalBillMapper autoMedicalBillMapper;
    private final ImageService imageService;

    private final AutoPrescribedDrugMapper autoPrescribedDrugMapper;
    private final AutoExaminationDetailMapper autoExaminationDetailMapper;

    private final Logger logger = LoggerFactory.getLogger(MedicalBillServiceImpl.class);

    @Override
    @Transactional
    public MedicalBillResponseDTO createMedicalBill(MedicalBillRequestDTO medicalBillRequestDTO) {
        //        Patient patient = patientRepository.findById(medicalBillRequestDTO.getPatientId())
        //                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id",
        // medicalBillRequestDTO.getPatientId()));
        //
        //        Doctor doctor = doctorRepository.findById(medicalBillRequestDTO.getDoctorId())
        //                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id",
        // medicalBillRequestDTO.getDoctorId()));
        //
        //        MedicalBill medicalBill = autoMedicalBillMapper.toEntity(medicalBillRequestDTO);
        //        medicalBill.setPatient(patient);
        //        medicalBill.setDoctor(doctor);
        //
        //        List<PrescribedDrug> prescribedDrugs =
        // prescribedDrugRepository.findAllById(medicalBillRequestDTO.getPrescribedDrugIds());
        //        if (prescribedDrugs.size() != medicalBillRequestDTO.getPrescribedDrugIds().size()) {
        //            throw new ResourceNotFoundException("One or more prescribed drugs not found");
        //        }
        //        medicalBill.setDrugs(prescribedDrugs);
        //        prescribedDrugs.forEach(drug -> drug.setMedicalBill(medicalBill));
        //
        //        MedicalBill savedMedicalBill = medicalBillRepository.save(medicalBill);
        //        return autoMedicalBillMapper.toResponseDTO(savedMedicalBill);
        MedicalBill medicalBill = autoMedicalBillMapper.toEntity(medicalBillRequestDTO);
        medicalBillRepository.save(medicalBill);
        return autoMedicalBillMapper.toResponseDTO(medicalBill);
    }

    @Override
    public MedicalBillResponseDTO createMedicalBillWithImage(
            MedicalBillRequestDTO medicalBillRequestDTO, List<MultipartFile> files) {
        MedicalBill medicalBill = autoMedicalBillMapper.toEntity(medicalBillRequestDTO);
        //        medicalBill = medicalBillRepository.save(medicalBill);

        List<Image> images =
                files.stream().map(imageService::convertMultipartFileToImage).toList();

        int imageIndex = 0;
        for (int i = 0;
                i < medicalBillRequestDTO.getExaminationDetailRequestDTOS().size();
                i++) {

            int count = medicalBillRequestDTO
                    .getExaminationDetailRequestDTOS()
                    .get(i)
                    .getImagesCount()
                    .intValue();
            List<Image> detailImages = images.subList(imageIndex, imageIndex + count);
            imageIndex += count;

            ExaminationDetail detail = medicalBill.getExaminationDetails().get(i);
            detail.addImage(detailImages);
        }

        medicalBillRepository.save(medicalBill);

        for (ExaminationDetail detail : medicalBill.getExaminationDetails()) {
            for (Image image : detail.getImagesTest()) {
                imageService.updateUrlDownload(image);
            }
        }

        return autoMedicalBillMapper.toResponseDTO(medicalBill);
    }

    @Override
    public MedicalBillResponseDTO createMedicalBillWithLabRequireRequest(
            MedicalBillWithLabRequestDTO medicalBillWithLabRequestDTO) {
        MedicalBill medicalBill =
                autoMedicalBillMapper.fromMedicalBillWithLabRequestDTOToEntity(medicalBillWithLabRequestDTO);
        medicalBillRepository.save(medicalBill);
        return autoMedicalBillMapper.toResponseDTO(medicalBill);
    }

    @Override
    public MedicalBillResponseDTO createMedicalBillWithPreExamination(
            MedicalBillWithPreExaminationDTO medicalBillWithPreExaminationDTO) {
        MedicalBill medicalBill =
                autoMedicalBillMapper.fromMedicalBillWithPreExaminationDTOToEntity(medicalBillWithPreExaminationDTO);
        medicalBillRepository.save(medicalBill);
        return autoMedicalBillMapper.toResponseDTO(medicalBill);
    }

    @Override
    public MedicalBillResponseDTO addDrugToMedicalBill(
            Long medicalBillId, List<PrescribedDrugRequestDTO> prescribedDrugRequestDTOS) {

        MedicalBill medicalBill = medicalBillRepository
                .findById(medicalBillId)
                .orElseThrow(() -> new ResourceNotFoundException("MedicalBill", "id", medicalBillId));

        List<PrescribedDrug> prescribedDrugs = prescribedDrugRequestDTOS.stream()
                .map(autoPrescribedDrugMapper::toEntity)
                .toList();

        medicalBill.addPrescribedDrugs(prescribedDrugs);

        return autoMedicalBillMapper.toResponseDTO(medicalBillRepository.save(medicalBill));
    }

    @Override
    public MedicalBillResponseDTO addLabRequestToMedicalBill(
            Long medicalBillId, List<ExaminationDetailLabRequestDTO> examinationDetailLabRequestDTOS) {
        MedicalBill medicalBill = medicalBillRepository
                .findById(medicalBillId)
                .orElseThrow(() -> new ResourceNotFoundException("MedicalBill", "id", medicalBillId));

        String patientName = medicalBill.getPatient().getFullName();
        String doctorName = medicalBill.getDoctor().getFullName();

        List<ExaminationDetail> examinationDetails = examinationDetailLabRequestDTOS.stream()
                .map(dto -> convertToExaminationDetail(dto, patientName, doctorName))
                .toList();

        medicalBill.addExaminationDetails(examinationDetails);
        MedicalBill savedMedicalBill = medicalBillRepository.save(medicalBill);
        return autoMedicalBillMapper.toResponseDTO(savedMedicalBill);
    }

    private ExaminationDetail convertToExaminationDetail(
            ExaminationDetailLabRequestDTO dto, String patientName, String doctorName) {
        ExaminationDetail detail = new ExaminationDetail();
        detail.setExaminationType(dto.getExaminationType());
        detail.setPatientName(patientName);
        detail.setDoctorName(doctorName);
        return detail;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalBillResponseDTO> getAllMedicalBills() {
        List<MedicalBill> medicalBills = medicalBillRepository.findAllWithPrescribedDrugs();
        return medicalBills.stream().map(autoMedicalBillMapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalBillResponseDTO getMedicalBillById(Long id) {
        MedicalBill medicalBill = medicalBillRepository
                .findByIdWithPrescribedDrugs(id)
                .orElseThrow(() -> new ResourceNotFoundException("MedicalBill", "id", id));
        return autoMedicalBillMapper.toResponseDTO(medicalBill);
    }

    @Override
    public Page<MedicalBillResponseDTO> getAllMedicalBills(Pageable pageable) {
        return medicalBillRepository.findAll(pageable).map(autoMedicalBillMapper::toResponseDTO);
    }

    @Override
    public List<MedicalBillResponseDTO> findMedicalBillsByPatientName(String patientName) {
        return medicalBillRepository.findByPatientFullNameContainingIgnoreCase(patientName).stream()
                .map(autoMedicalBillMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalBillResponseDTO> findMedicalBillsByDoctorName(String doctorName) {
        return medicalBillRepository.findByDoctorFullNameContainingIgnoreCase(doctorName).stream()
                .map(autoMedicalBillMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<MedicalBillResponseDTO> findMedicalBillsByPatientId(Long patientId, Pageable pageable) {
        return medicalBillRepository.findByPatientId(patientId, pageable).map(autoMedicalBillMapper::toResponseDTO);
    }

    @Override
    public Page<MedicalBillResponseDTO> findMedicalBillsByDoctorId(Long doctorId, Pageable pageable) {
        return medicalBillRepository.findByDoctorId(doctorId, pageable).map(autoMedicalBillMapper::toResponseDTO);
    }

    @Override
    @Transactional
    public MedicalBillResponseDTO updateMedicalBill(Long id, MedicalBillRequestDTO medicalBillRequestDTO) {

        MedicalBill medicalBill = medicalBillRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("medical bill", "id", id));
        autoMedicalBillMapper.updateMedicalBillFromDTO(medicalBillRequestDTO, medicalBill);
        medicalBillRepository.save(medicalBill);
        MedicalBillResponseDTO medicalBillResponseDTO = autoMedicalBillMapper.toResponseDTO(medicalBill);

        return medicalBillResponseDTO;
    }

    @Override
    @Transactional
    public void deleteMedicalBill(Long id) {
        MedicalBill medicalBill = medicalBillRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MedicalBill", "id", id));
        medicalBillRepository.delete(medicalBill);
    }

    @Override
    public MedicalBillResponseDTO getTopMedicalBillByPatientId(Long patientId) {
        return medicalBillRepository
                .findTopByPatientIdOrderByIdDesc(patientId)
                .map(autoMedicalBillMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("MedicalBill", "patientId", patientId));
    }

    @Override
    public MedicalBillResponseDTO partialUpdateMedicalBill(Long id, MedicalBillPartialUpdateRequestDTO dto) {
        return medicalBillRepository
                .findById(id)
                .map(medicalBill -> {
                    autoMedicalBillMapper.partialUpdateMedicalBillFromDTO(dto, medicalBill);
                    return autoMedicalBillMapper.toResponseDTO(medicalBillRepository.save(medicalBill));
                })
                .orElseThrow(() -> new ResourceNotFoundException("MedicalBill", "id", id));
    }

    @Transactional
    @Override
    public MedicalBillResponseDTO updateExaminationDetailsStatusAndCalculateTotal(Long medicalBillId) {
        // Fetch the medical bill by ID
        MedicalBill medicalBill = medicalBillRepository
                .findById(medicalBillId)
                .orElseThrow(() -> new IllegalArgumentException("MedicalBill not found with id: " + medicalBillId));

        // Update the status of all UNPAID examination details to PAID
        List<ExaminationDetail> examinationDetails = medicalBill.getExaminationDetails();
        examinationDetails.stream()
                .filter(detail -> detail.getStatus().equals(ExaminationDetailStatus.UNPAID))
                .forEach(detail -> detail.setStatus(ExaminationDetailStatus.PAID));

        // Save the updated examination details
        //        examinationRepository.saveAll(examinationDetails);

        // Map the updated medical bill to a response DTO
        MedicalBillResponseDTO responseDTO = autoMedicalBillMapper.toResponseDTO(medicalBill);

        // Save the updated medical bill
        medicalBillRepository.save(medicalBill);

        return responseDTO;
    }

    @Override
    public Page<MedicalBillResponseDTO> getMedicalBillsByDateAndUnpaidExaminationDetails(LocalDate date, Pageable pageable) {
        return medicalBillRepository.findByDateAndUnpaidExaminationDetails(date, pageable)
                .map(autoMedicalBillMapper::toResponseDTO);
    }
}
