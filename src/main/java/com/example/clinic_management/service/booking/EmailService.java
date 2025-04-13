package com.example.clinic_management.service.booking;

import com.example.clinic_management.entities.Appointment;

public interface EmailService {
    void sendSimpleEmail(String toEmail, String subject, String body);

    public void sendHtmlFormatSuccessPayment(String toEmail, String subject, Appointment appointment, String amount);

    public void sendAppointmentReminders();
}
