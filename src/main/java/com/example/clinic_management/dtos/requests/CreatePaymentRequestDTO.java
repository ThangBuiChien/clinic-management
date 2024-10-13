package com.example.clinic_management.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePaymentRequestDTO {
    private String status;
    private String message;
    private String url;
}
