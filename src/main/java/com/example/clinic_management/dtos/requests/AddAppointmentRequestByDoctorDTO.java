package com.example.clinic_management.dtos.requests;

import com.example.clinic_management.enums.AppointmentStatus;
import com.example.clinic_management.enums.TimeSlot;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AddAppointmentRequestByDoctorDTO {
    private Long doctorId;
    private Long patientId;
    private LocalDate appointmentDate;
    private TimeSlot timeSlot;
}
