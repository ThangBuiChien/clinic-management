package com.example.clinic_management.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.clinic_management.config.PaymentConfig;
import com.example.clinic_management.dtos.requests.CreatePaymentRequestDTO;
import com.example.clinic_management.dtos.responses.TransactionStatusResponseDTO;
import com.example.clinic_management.entities.Appointment;
import com.example.clinic_management.enums.AppointmentStatus;
import com.example.clinic_management.exception.ResourceNotFoundException;
import com.example.clinic_management.mapper.AutoAppointmentMapper;
import com.example.clinic_management.repository.AppointmentRepository;
import com.example.clinic_management.service.booking.EmailService;
import com.example.clinic_management.service.booking.PaymentVNPayService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentVNPaySeriviceImpl implements PaymentVNPayService {

    private final AppointmentRepository appointmentRepository;
    private final AutoAppointmentMapper autoAppointmentMapper;
    private final EmailService emailService;

    @Override
    public CreatePaymentRequestDTO createPaymentRequest(HttpServletRequest req, Long appointmentId)
            throws UnsupportedEncodingException {
        Appointment appointment = appointmentRepository
                .findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("appointment", "id", appointmentId));
        if (appointment.getAppointmentStatus() != AppointmentStatus.PENDING) {
            throw new RuntimeException("Appointment has been pay!");
        }

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";

        long amount = 70000 * 100;
        String bankCode = "NCB";

        String vnp_TxnRef = PaymentConfig.getRandomNumber(8);

        String vnp_TmnCode = PaymentConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = req.getParameter("language");
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", PaymentConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", PaymentConfig.getIpAddress(req));

//        TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
//        Calendar cld = Calendar.getInstance(utcTimeZone);
//        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        log.info("vnp_CreateDate: {}", vnp_CreateDate);
        log.info("timezone: {}", cld.getTimeZone().getID());

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = PaymentConfig.hmacSHA512(PaymentConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = PaymentConfig.vnp_PayUrl + "?" + queryUrl;

        CreatePaymentRequestDTO paymentRequestDTO = new CreatePaymentRequestDTO();
        paymentRequestDTO.setStatus("OK");
        paymentRequestDTO.setMessage("Success");
        paymentRequestDTO.setUrl(paymentUrl);

        appointment.setPayId(Long.parseLong(vnp_TxnRef));
        appointmentRepository.save(appointment);
        return paymentRequestDTO;
    }

    @Transactional
    @Override
    public TransactionStatusResponseDTO handleTransactionResult(
            String amount, String bankCode, String order, String responseCode) {
        TransactionStatusResponseDTO transactionStatusDTO = new TransactionStatusResponseDTO();
        if (responseCode.equals("00")) {
            String decodedOrderInfo = java.net.URLDecoder.decode(order, StandardCharsets.UTF_8);
            String orderIdStr = decodedOrderInfo.substring(decodedOrderInfo.indexOf(":") + 1);
            Long orderId = Long.parseLong(orderIdStr);
            Appointment appointment = appointmentRepository
                    .findByPayId(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException("appointment", "payId", orderId));
            appointment.setAppointmentStatus(AppointmentStatus.CONFIRMED);
            appointmentRepository.save(appointment);

            // send mail
            //            emailService.sendSimpleEmail(appointment.getPatient().getEmail(), "Payment success",
            //                    "Your payment amount " + formatAmount(amount) + " VND for appointment with id "
            //                            + appointment.getId() + " was successfully");

            // send mail with html
            emailService.sendHtmlFormatSuccessPayment(
                    appointment.getPatient().getEmail(), "Payment success", appointment, formatAmount(amount));

            transactionStatusDTO.setStatus("Ok");
            transactionStatusDTO.setMessage("Successfully");
            transactionStatusDTO.setData(autoAppointmentMapper.toResponseDTO(appointment));
        } else {
            transactionStatusDTO.setStatus("No");
            transactionStatusDTO.setMessage("Failed");
            transactionStatusDTO.setData("");
        }
        return transactionStatusDTO;
    }

    public String formatAmount(String amount) {
        // Convert amount to long and remove the last two digits
        long amountLong = Long.parseLong(amount) / 100;
        String amountStr = String.valueOf(amountLong);

        // Create a StringBuilder to build the formatted string
        StringBuilder formattedAmount = new StringBuilder();

        // Insert dots every three digits from the right
        int length = amountStr.length();
        for (int i = 0; i < length; i++) {
            if (i > 0 && (length - i) % 3 == 0) {
                formattedAmount.append('.');
            }
            formattedAmount.append(amountStr.charAt(i));
        }

        return formattedAmount.toString();
    }
}
