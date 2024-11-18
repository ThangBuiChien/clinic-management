package com.example.clinic_management.controllers;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.clinic_management.dtos.responses.ApiResponse;
import com.example.clinic_management.service.ScheduleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/doctor/{doctorId}/date/{date}")
    public ResponseEntity<ApiResponse> getSchedulesByDoctorIdAndDate(
            @PathVariable Long doctorId, @PathVariable LocalDate date) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Schedules fetched successfully")
                .result(scheduleService.getSchedulesByDoctorIdAndDate(doctorId, date))
                .build());
    }
}
