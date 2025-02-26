package com.example.clinic_management.mapper;

import com.example.clinic_management.dtos.requests.MedicalBillWithPreExaminationDTO;
import org.mapstruct.*;

import com.example.clinic_management.dtos.requests.MedicalBillRequestDTO;
import com.example.clinic_management.dtos.requests.MedicalBillWithLabRequestDTO;
import com.example.clinic_management.dtos.responses.MedicalBillResponseDTO;
import com.example.clinic_management.entities.ExaminationDetail;
import com.example.clinic_management.entities.MedicalBill;
import com.example.clinic_management.entities.PrescribedDrug;

@Mapper(
        componentModel = "spring",
        uses = {AutoPrescribedDrugMapper.class, AutoExaminationDetailMapper.class, MapperService.class})
public interface AutoMedicalBillMapper {

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientName", source = "patient.fullName")
    @Mapping(target = "patientGender", source = "patient.gender")
    @Mapping(target = "patientBirthDate", source = "patient.birthDate")
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorName", source = "doctor.fullName")
    @Mapping(target = "prescribedDrugs", source = "drugs")
    MedicalBillResponseDTO toResponseDTO(MedicalBill medicalBill);

    @Mapping(target = "doctor", source = "doctorId")
    @Mapping(target = "patient", source = "patientId")
    @Mapping(target = "drugs", source = "prescribedDrugRequestDTOS")
    @Mapping(target = "examinationDetails", source = "examinationDetailRequestDTOS")
    MedicalBill toEntity(MedicalBillRequestDTO medicalBillRequestDTO);

    @Mapping(target = "doctor", source = "doctorId")
    @Mapping(target = "patient", source = "patientId")
    @Mapping(target = "examinationDetails", source = "examinationDetailLabRequestDTOS")
    MedicalBill fromMedicalBillWithLabRequestDTOToEntity(MedicalBillWithLabRequestDTO medicalBillWithLabRequestDTO);

    @Mapping(target = "doctor", source = "doctorId")
    @Mapping(target = "patient", source = "patientId")
    @Mapping(target = "drugs", source = "prescribedDrugRequestDTOS")
    @Mapping(target = "examinationDetails", source = "examinationDetailRequestDTOS")
    void updateMedicalBillFromDTO(MedicalBillRequestDTO medicalBillRequestDTO, @MappingTarget MedicalBill medicalBill);

    @Mapping(target = "doctor", source = "doctorId")
    @Mapping(target = "patient", source = "patientId")
    MedicalBill fromMedicalBillWithPreExaminationDTOToEntity(MedicalBillWithPreExaminationDTO medicalBillWithPreExaminationDTO);

    // In bi-directional mapping, so we need two handle 2 sides
    // medicalBill.addPreDrug() and also preDrug.setMedicalBill();
    // mapstruct only do only one side, so we need to do the other side
    @AfterMapping
    default void linkDrugsAndExaminationDetailToMedicalBill(@MappingTarget MedicalBill medicalBill) {
        if (medicalBill.getDrugs() != null) {
            for (PrescribedDrug drug : medicalBill.getDrugs()) {
                drug.setMedicalBill(medicalBill);
            }
        }
        if (medicalBill.getExaminationDetails() != null) {
            for (ExaminationDetail examinationDetail : medicalBill.getExaminationDetails()) {
                examinationDetail.setPatientName(medicalBill.getPatient().getFullName());
                examinationDetail.setDoctorName(medicalBill.getDoctor().getFullName());
                examinationDetail.setMedicalBill(medicalBill);
            }
        }
    }
}
