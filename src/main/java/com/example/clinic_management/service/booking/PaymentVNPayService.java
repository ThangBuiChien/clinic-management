package com.example.clinic_management.service.booking;

import java.io.UnsupportedEncodingException;

import jakarta.servlet.http.HttpServletRequest;

import com.example.clinic_management.dtos.requests.CreatePaymentRequestDTO;
import com.example.clinic_management.dtos.responses.TransactionStatusResponseDTO;

public interface PaymentVNPayService {
    CreatePaymentRequestDTO createPaymentRequest(HttpServletRequest req, Long appointmentId)
            throws UnsupportedEncodingException;

    TransactionStatusResponseDTO handleTransactionResult(
            String amount, String bankCode, String order, String responseCode);
}
