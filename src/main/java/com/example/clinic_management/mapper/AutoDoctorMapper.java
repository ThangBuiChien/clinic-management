package com.example.clinic_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.clinic_management.dtos.requests.DoctorPartialUpdateDTO;
import com.example.clinic_management.dtos.requests.DoctorRequestDTO;
import com.example.clinic_management.dtos.responses.DoctorResponseDTO;
import com.example.clinic_management.entities.Doctor;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {AutoDepartmentMapper.class, MapperService.class})
public interface AutoDoctorMapper {

    //    @Mapping(source = "departmentId", target = "department")
    @Mapping(target = "department", ignore = true)
    Doctor toEntity(DoctorRequestDTO doctorRequestDTO);

    @Mapping(source = "department.id", target = "departmentId")
    DoctorResponseDTO toResponseDTO(Doctor doctor);

    @Mapping(source = "departmentId", target = "department")
    @Mapping(target = "email", ignore = true)
    void updateFromDTO(DoctorRequestDTO doctorRequestDTO, @MappingTarget Doctor doctor);

    @Mapping(source = "departmentId", target = "department")
    @Mapping(target = "email", ignore = true)
    void updatePartialFromDTO(DoctorPartialUpdateDTO doctorPartialUpdateDTO, @MappingTarget Doctor doctor);
}
