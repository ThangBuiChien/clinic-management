package com.example.clinic_management.mapper;

import org.mapstruct.Mapper;

import com.example.clinic_management.dtos.responses.DoctorTimeslotCapacityResponseDTO;
import com.example.clinic_management.entities.DoctorTimeslotCapacity;

@Mapper(componentModel = "spring")
public interface AutoDoctorTimeSlotMapper {

    DoctorTimeslotCapacityResponseDTO toResponseDTO(DoctorTimeslotCapacity doctorTimeslotCapacity);
}
