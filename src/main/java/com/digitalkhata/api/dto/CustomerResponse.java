package com.digitalkhata.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDateTime lastTransactionDate;
    private LocalDateTime createdAt;
    private Double pendingAmount; // Will calculate from transactions
}