package com.example.clinic_management.service.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.clinic_management.dtos.requests.FeedbackCreateDTO;
import com.example.clinic_management.dtos.responses.FeedbackResponseDTO;

public interface FeedbackService {

    FeedbackResponseDTO createFeedback(Long appointmentId, FeedbackCreateDTO feedbackCreateDTO);

    Page<FeedbackResponseDTO> getFeedbackByDoctorId(Long doctorId, Pageable pageable);

    Page<FeedbackResponseDTO> getFeedbackByPatientId(Long patientId, Pageable pageable);
}
