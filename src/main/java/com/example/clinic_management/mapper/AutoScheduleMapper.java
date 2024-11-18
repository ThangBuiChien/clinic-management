package com.example.clinic_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.clinic_management.dtos.responses.ScheduleResponseDTO;
import com.example.clinic_management.entities.Schedule;

@Mapper(
        componentModel = "spring",
        uses = {AutoDoctorTimeSlotMapper.class})
public interface AutoScheduleMapper {

    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorTimeslotCapacityResponseDTOS", source = "doctorTimeslotCapacitySet")
    ScheduleResponseDTO toResponseDTO(Schedule schedule);
}
