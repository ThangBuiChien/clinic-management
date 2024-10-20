package com.example.clinic_management.mapper;

import com.example.clinic_management.dtos.requests.MedicalBillRequestDTO;
import com.example.clinic_management.dtos.responses.MedicalBillResponseDTO;
import com.example.clinic_management.entities.MedicalBill;
import com.example.clinic_management.entities.PrescribedDrug;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {AutoPrescribedDrugMapper.class,
        MapperService.class})
public interface AutoMedicalBillMapper {

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientName", source = "patient.fullName")
    @Mapping(target = "patientGender", source = "patient.gender")
    @Mapping(target = "patientBirthDate", source = "patient.birthDate")
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorName", source = "doctor.fullName")
    @Mapping(target = "prescribedDrugs", source = "drugs")
    MedicalBillResponseDTO toResponseDTO(MedicalBill medicalBill);

//    @Mapping(target = "patient", ignore = true)
//    @Mapping(target = "doctor", ignore = true)
//    @Mapping(target = "drugs", ignore = true)
    @Mapping(target = "doctor", source = "doctorId")
    @Mapping(target = "patient", source = "patientId")
    @Mapping(target = "drugs", source = "prescribedDrugRequestDTOS")
    MedicalBill toEntity(MedicalBillRequestDTO medicalBillRequestDTO);

    // In bi-directional mapping, so we need two handle 2 sides
    // medicalBill.addPreDrug() and also preDrug.setMedicalBill();
    // mapstruct only do only one side, so we need to do the other side
    @AfterMapping
    default void linkDrugsToMedicalBill(@MappingTarget MedicalBill medicalBill) {
        if (medicalBill.getDrugs() != null) {
            for (PrescribedDrug drug : medicalBill.getDrugs()) {
                drug.setMedicalBill(medicalBill);
            }
        }
    }

    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "drugs", ignore = true)
    void updateMedicalBillFromDTO(MedicalBillRequestDTO medicalBillRequestDTO, @MappingTarget MedicalBill medicalBill);
}