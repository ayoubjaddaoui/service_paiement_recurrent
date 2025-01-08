package com.example.servicepaiementrecurrent.controller;

import com.example.servicepaiementrecurrent.dto.PaiementRecurrentDTO;
import com.example.servicepaiementrecurrent.service.PaiementRecurrentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/paiements-recurrents")
public class PaiementRecurrentController {
    @Autowired
    private PaiementRecurrentService service;

    @PostMapping
    public ResponseEntity<String> createRecurringPayment(@RequestBody PaiementRecurrentDTO dto) {
        service.createRecurringPayment(dto); // Création du paiement récurrent
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Paiement a été créé avec succès");
    }

}
