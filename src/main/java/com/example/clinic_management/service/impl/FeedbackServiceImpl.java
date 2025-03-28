package com.example.clinic_management.service.impl;

import com.example.clinic_management.dtos.requests.FeedbackCreateDTO;
import com.example.clinic_management.dtos.responses.FeedbackResponseDTO;
import com.example.clinic_management.entities.Appointment;
import com.example.clinic_management.entities.Feedback;
import com.example.clinic_management.mapper.AutoDoctorMapper;
import com.example.clinic_management.repository.AppointmentRepository;
import com.example.clinic_management.repository.FeedbackRepository;
import com.example.clinic_management.service.booking.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final AppointmentRepository appointmentRepository;
    private final AutoDoctorMapper autoDoctorMapper;
    @Override
    public FeedbackResponseDTO createFeedback(Long appointmentId, FeedbackCreateDTO feedbackCreateDTO) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        Feedback feedback = new Feedback();
        feedback.setAppointment(appointment);
        feedback.setDoctor(appointment.getDoctor());
        feedback.setRating(feedbackCreateDTO.getRating());
        feedback.setComment(feedbackCreateDTO.getComment());
        feedback.setPatient(appointment.getPatient());

        feedbackRepository.save(feedback);

        return convertToDTO(feedback);
    }

    @Override
    public Page<FeedbackResponseDTO> getFeedbackByDoctorId(Long doctorId, Pageable pageable) {
        Page<Feedback> feedbacks = feedbackRepository.findAllByDoctorId(doctorId, pageable);
        return feedbacks.map(this::convertToDTO);
    }

    @Override
    public Page<FeedbackResponseDTO> getFeedbackByPatientId(Long patientId, Pageable pageable) {
        Page<Feedback> feedbacks = feedbackRepository.findAllByPatientId(patientId, pageable);
        return feedbacks.map(this::convertToDTO);
    }

    private FeedbackResponseDTO convertToDTO(Feedback feedback) {
        return FeedbackResponseDTO.builder()
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .createdAt(feedback.getCreatedAt())
                .patientName(feedback.getPatient().getFullName())
                .doctorDepartmentName(feedback.getDoctor().getDepartment().getName())
                .doctorResponseDTO(autoDoctorMapper.toResponseDTO(feedback.getDoctor()))
                .build();
    }
}
