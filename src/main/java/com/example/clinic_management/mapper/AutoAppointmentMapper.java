package com.example.clinic_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.clinic_management.dtos.requests.AddAppointmentRequestByDoctorDTO;
import com.example.clinic_management.dtos.requests.AppointmentRequestDTO;
import com.example.clinic_management.dtos.responses.AppointmentResponseDTO;
import com.example.clinic_management.entities.Appointment;

@Mapper(
        componentModel = "spring",
        uses = {MapperService.class})
public interface AutoAppointmentMapper {

    @Mapping(source = "doctorId", target = "doctor")
    @Mapping(source = "patientId", target = "patient")
    Appointment toEntity(AppointmentRequestDTO appointmentRequestDTO);

    @Mapping(source = "doctorId", target = "doctor")
    @Mapping(source = "patientId", target = "patient")
    Appointment toEntity(AddAppointmentRequestByDoctorDTO addAppointmentRequestByDoctorDTO);

    @Mapping(source = "doctor.fullName", target = "doctorName")
    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "patient.fullName", target = "patientName")
    @Mapping(source = "patient.id", target = "patientId")
    AppointmentResponseDTO toResponseDTO(Appointment appointment);
}
