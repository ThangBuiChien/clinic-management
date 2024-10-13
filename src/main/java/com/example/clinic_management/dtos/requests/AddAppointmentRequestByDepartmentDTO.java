package com.example.clinic_management.dtos.requests;

import com.example.clinic_management.enums.AppointmentStatus;
import com.example.clinic_management.enums.TimeSlot;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AddAppointmentRequestByDepartmentDTO {
    private Long patientId;
    private Long departmentId;
    private LocalDate appointmentDate;
    private TimeSlot timeSlot;
}
