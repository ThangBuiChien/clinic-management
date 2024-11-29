package com.example.clinic_management.controllers;

import java.io.UnsupportedEncodingException;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.clinic_management.dtos.requests.CreatePaymentRequestDTO;
import com.example.clinic_management.dtos.responses.TransactionStatusResponseDTO;
import com.example.clinic_management.service.PaymentVNPayService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentVNPayService paymentVNPayService;

    @GetMapping("/create_payment/{appointmentId}")
    public ResponseEntity<?> createPayment(HttpServletRequest req, @PathVariable Long appointmentId)
            throws UnsupportedEncodingException {
        CreatePaymentRequestDTO paymentRequestDTO = paymentVNPayService.createPaymentRequest(req, appointmentId);

        return ResponseEntity.ok().body(paymentRequestDTO);
    }

    @GetMapping("/payment_info")
    public ResponseEntity<?> transaction(
            @RequestParam(value = "vnp_Amount") String amount,
            @RequestParam(value = "vnp_BankCode") String bankCode,
            @RequestParam(value = "vnp_OrderInfo") String order,
            @RequestParam(value = "vnp_ResponseCode") String responseCode) {
        TransactionStatusResponseDTO transactionStatusDTO =
                paymentVNPayService.handleTransactionResult(amount, bankCode, order, responseCode);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "http://localhost:5173/payment-success")
                .body(transactionStatusDTO);
    }
}
