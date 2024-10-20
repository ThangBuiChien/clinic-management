package com.example.clinic_management.mapper;

import com.example.clinic_management.dtos.requests.MedicalBillRequestDTO;
import com.example.clinic_management.dtos.responses.MedicalBillResponseDTO;
import com.example.clinic_management.entities.MedicalBill;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {AutoPrescribedDrugMapper.class})
public interface AutoMedicalBillMapper {

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientName", source = "patient.fullName")
    @Mapping(target = "patientGender", source = "patient.gender")
    @Mapping(target = "patientBirthDate", source = "patient.birthDate")
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorName", source = "doctor.fullName")
    @Mapping(target = "prescribedDrugs", source = "drugs")
    MedicalBillResponseDTO toResponseDTO(MedicalBill medicalBill);

    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "drugs", ignore = true)
    MedicalBill toEntity(MedicalBillRequestDTO medicalBillRequestDTO);

    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "drugs", ignore = true)
    void updateMedicalBillFromDTO(MedicalBillRequestDTO medicalBillRequestDTO, @MappingTarget MedicalBill medicalBill);
}