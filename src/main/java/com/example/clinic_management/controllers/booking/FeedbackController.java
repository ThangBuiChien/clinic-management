package com.example.clinic_management.controllers.booking;

import com.example.clinic_management.dtos.requests.FeedbackCreateDTO;
import com.example.clinic_management.dtos.responses.ApiResponse;
import com.example.clinic_management.dtos.responses.FeedbackResponseDTO;
import com.example.clinic_management.service.booking.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/{appointmentId}")
    public ResponseEntity<ApiResponse> addFeedback(
            @PathVariable Long appointmentId, @RequestBody @Valid FeedbackCreateDTO feedbackCreateDTO) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Feedback added successfully")
                .result(feedbackService.createFeedback(appointmentId, feedbackCreateDTO))
                .build());
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<ApiResponse> getFeedbackByDoctorId(@PathVariable Long doctorId,
                                                             Pageable pageable) {
        Page<FeedbackResponseDTO> feedbackList = feedbackService.getFeedbackByDoctorId(doctorId, pageable);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Feedback fetched successfully")
                .result(feedbackList)
                .build());
    }


}
