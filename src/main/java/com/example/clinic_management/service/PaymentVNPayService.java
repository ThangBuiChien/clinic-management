package com.example.clinic_management.service;

import com.example.clinic_management.dtos.requests.CreatePaymentRequestDTO;
import com.example.clinic_management.dtos.responses.TransactionStatusResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;

public interface PaymentVNPayService {
    CreatePaymentRequestDTO createPaymentRequest(HttpServletRequest req, Long appointmentId) throws UnsupportedEncodingException;

    TransactionStatusResponseDTO handleTransactionResult(String amount, String bankCode, String order, String responseCode);
}
