package com.example.clinic_management.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import com.example.clinic_management.entities.Appointment;
import com.example.clinic_management.repository.AppointmentRepository;
import com.example.clinic_management.service.booking.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    private final AppointmentRepository appointmentRepository;

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${spring.mail.username}")
    private String setFrom;

    @Async
    @Override
    public void sendSimpleEmail(String toEmail, String subject, String body) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(setFrom);
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);

        javaMailSender.send(simpleMailMessage);
    }

    @Async
    @Override
    public void sendHtmlFormatSuccessPayment(String toEmail, String subject, Appointment appointment, String amount) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(setFrom);
            helper.setTo(toEmail);
            helper.setSubject(subject);

            // Load HTML template
            ClassPathResource resource = new ClassPathResource("static/payment-template.html");
            String htmlTemplate = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

            // Replace placeholders with actual values
            String htmlContent = htmlTemplate
                    .replace("${customerName}", appointment.getPatient().getFullName())
                    .replace("${amount}", amount)
                    .replace("${appointmentId}", appointment.getId().toString())
                    .replace(
                            "${appointmentDate}",
                            appointment.getAppointmentDate().toString())
                    .replace("${timeSlot}", appointment.getTimeSlot().getSlot())
                    .replace("${doctorName}", appointment.getDoctor().getFullName())
                    .replace(
                            "${appointmentStatus}",
                            appointment.getAppointmentStatus().toString());

            // Set the HTML content
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        } catch (MessagingException | IOException e) {
            logger.error("Failed to send HTML format success payment email to {}", toEmail, e);
        }
    }

    @Scheduled(cron = "0 0 8 * * *")
    @Transactional
    @Override
    public void sendAppointmentReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        List<Appointment> appointments = appointmentRepository.findByAppointmentDate(tomorrow);
        for (Appointment appointment : appointments) {
            String toEmail = appointment.getPatient().getEmail();
            String subject = "Appointment Reminder";
            String body = "Dear " + appointment.getPatient().getFullName() + ",\n\n"
                    + "This is a reminder for your appointment with Dr. "
                    + appointment.getDoctor().getFullName() + " on "
                    + appointment.getAppointmentDate() + " at "
                    + appointment.getTimeSlot().getSlot() + ".\n\n"
                    + "Thank you,\n"
                    + "Clinic Management System";

            sendSimpleEmail(toEmail, subject, body);
        }
    }
}
