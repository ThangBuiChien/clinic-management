package com.example.clinic_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.clinic_management.dtos.requests.DoctorRequestDTO;
import com.example.clinic_management.dtos.responses.DoctorResponseDTO;
import com.example.clinic_management.entities.Doctor;

@Mapper(
        componentModel = "spring",
        uses = {AutoDepartmentMapper.class, MapperService.class})
public interface AutoDoctorMapper {

    @Mapping(source = "departmentId", target = "department")
    Doctor toEntity(DoctorRequestDTO doctorRequestDTO);

    @Mapping(source = "department.id", target = "departmentId")
    DoctorResponseDTO toResponseDTO(Doctor doctor);
}
