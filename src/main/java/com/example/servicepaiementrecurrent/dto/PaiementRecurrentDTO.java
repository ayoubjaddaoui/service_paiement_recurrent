package com.example.servicepaiementrecurrent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaiementRecurrentDTO {
        private Long id;
        private Long userId;
        private String paymentMethod;
        private Fournisseur fournisseur;
        private Devise currency;
        private Double amount;
        private String frequency; // DAILY, WEEKLY, MONTHLY, YEARLY
        private LocalDate startDate;
        private String status;
        private LocalDate nextExecutionDate;


}
