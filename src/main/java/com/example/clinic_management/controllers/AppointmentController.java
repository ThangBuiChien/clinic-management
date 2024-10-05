package com.example.clinic_management.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.clinic_management.dtos.requests.AppointmentRequestDTO;
import com.example.clinic_management.dtos.responses.ApiResponse;
import com.example.clinic_management.enums.AppointmentStatus;
import com.example.clinic_management.service.AppointmentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/appointment")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping("")
    public ResponseEntity<ApiResponse> addDepartment(@Valid @RequestBody AppointmentRequestDTO appointmentRequestDTO) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Appointment created successfully")
                .result(appointmentService.addAppointment(appointmentRequestDTO))
                .build());
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse> getAllAppointments(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("All appointments fetched successfully")
                .result(appointmentService.getAllAppointments(pageable))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Appointment fetched successfully")
                .result(appointmentService.getAppointmentById(id))
                .build());
    }

    @GetMapping("/doctor/{doctorId}/date/{date}")
    public ResponseEntity<ApiResponse> getAppointmentByDoctorIdAndDate(
            @PathVariable Long doctorId, @PathVariable String date) {

        // convert date string to LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);

        return ResponseEntity.ok(ApiResponse.builder()
                .message("Appointment fetched successfully")
                .result(appointmentService.getAppointmentByDoctorIdAndDate(doctorId, localDate))
                .build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse> updateAppointmentStatus(
            @PathVariable Long id, @RequestBody AppointmentStatus newStatus) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Appointment status updated successfully")
                .result(appointmentService.updateAppointmentStatus(id, newStatus))
                .build());
    }
}
