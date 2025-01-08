package com.example.servicepaiementrecurrent.clients;

import com.example.servicepaiementrecurrent.dto.CarteVirtuelleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "service-cartes-virtuelles", url = "https://servicecartesvirtuelles-production.up.railway.app/api/cartes-virtuelles")
public interface CarteVirtuelleClient {

    @GetMapping("/{cvv}")
    CarteVirtuelleDTO getCarteByCvv(@PathVariable String cvv);

    @PutMapping("/{id}/debit")
    void debitCard(@PathVariable Long id, @RequestParam Double amount);

    @GetMapping("/id-by-cvv")
    Long getCardIdByCvv(@RequestParam String cvv);

    @GetMapping("/utilisateur/{utilisateurId}")
    ResponseEntity<List<CarteVirtuelleDTO>> getCartesParUtilisateur(@PathVariable Long utilisateurId);


}
