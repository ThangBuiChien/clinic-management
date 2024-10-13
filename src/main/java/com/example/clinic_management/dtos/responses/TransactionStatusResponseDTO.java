package com.example.clinic_management.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionStatusResponseDTO {
    String status;
    String message;
    Object data;
}
