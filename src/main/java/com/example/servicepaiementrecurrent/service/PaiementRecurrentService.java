package com.example.servicepaiementrecurrent.service;

import com.example.servicepaiementrecurrent.clients.CarteVirtuelleClient;
import com.example.servicepaiementrecurrent.dto.CarteVirtuelleDTO;
import com.example.servicepaiementrecurrent.dto.Devise;
import com.example.servicepaiementrecurrent.dto.PaiementRecurrentDTO;
import com.example.servicepaiementrecurrent.entities.PaiementRecurrent;
import com.example.servicepaiementrecurrent.entities.Utilisateur;
import com.example.servicepaiementrecurrent.mapper.PaiementMapper;
import com.example.servicepaiementrecurrent.repository.PaiementRecurrentRepository;
import com.example.servicepaiementrecurrent.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PaiementRecurrentService {

    @Autowired
    private PaiementRecurrentRepository repository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private PaiementMapper mapper;

    private CarteVirtuelleClient client;


    public PaiementRecurrentDTO createRecurringPayment(PaiementRecurrentDTO dto) {
        PaiementRecurrent payment = new PaiementRecurrent();
        Utilisateur utilisateur = utilisateurRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID : " + dto.getUserId()));
        payment.setFournisseur(dto.getFournisseur());
        payment.setUtilisateur(utilisateur);
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setCurrency(dto.getCurrency());
        payment.setAmount(dto.getAmount());
        payment.setFrequency(dto.getFrequency());
        payment.setStartDate(dto.getStartDate());
        payment.setNextExecutionDate(calculateNextExecutionDate(dto.getStartDate(), dto.getFrequency()));
        payment.setStatus("ACTIVE");


        System.out.println("user : " +utilisateur);
        PaiementRecurrent savedPayment = repository.save(payment);

        return mapper.toDTO(savedPayment);
    }

    private LocalDate calculateNextExecutionDate(LocalDate startDate, String frequency) {
        switch (frequency.toUpperCase()) {
            case "QUOTIDIEN":
                return startDate.plusDays(1);
            case "HEBDOMADAIRE":
                return startDate.plusWeeks(1);
            case "MENSUEL":
                return startDate.plusMonths(1);
            case "ANNUEL":
                return startDate.plusYears(1);
            default:
                throw new IllegalArgumentException("Fréquence invalide : " + frequency);
        }
    }

    // Method to process recurring payment using virtual card

    // Méthode de planification pour traiter les paiements récurrents
    @Scheduled(cron = "0 0 0 * * ?")
    public void processRecurringPaymentsWithVirtualCards() {
        List<Long> paiementIds = getPaymentsFromQueue(); // Récupérer les IDs de la base de données ou de la file d'attente

        for (Long paiementRecurrentId : paiementIds) {
            try {
                // Retrieve the payment record by its ID
                PaiementRecurrent paiementRecurrent = repository.findById(paiementRecurrentId)
                        .orElseThrow(() -> new RuntimeException("Paiement récurrent non trouvé avec l'ID : " + paiementRecurrentId));

                // Get the list of virtual cards for the user
                List<CarteVirtuelleDTO> cartes = (List<CarteVirtuelleDTO>) client.getCartesParUtilisateur(paiementRecurrent.getUtilisateur().getId());

                // Find the correct card based on the currency
                CarteVirtuelleDTO carte = findCardByCurrency(cartes, paiementRecurrent.getCurrency());

                // If the card is found, process the payment
                if (carte != null) {
                    // Debit the card with the specified amount
                    client.debitCard(carte.getId(), paiementRecurrent.getAmount());
                    System.out.println("Montant débité avec succès : " + paiementRecurrent.getAmount());

                    // Update the next execution date based on frequency
                    LocalDate nextExecutionDate = calculateNextExecutionDate(paiementRecurrent.getNextExecutionDate(), paiementRecurrent.getFrequency());
                    paiementRecurrent.setNextExecutionDate(nextExecutionDate);

                    // Save the updated payment record
                    repository.save(paiementRecurrent);
                } else {
                    throw new RuntimeException("Carte virtuelle non trouvée pour l'utilisateur avec la devise " + paiementRecurrent.getCurrency());
                }
            } catch (Exception e) {
                // Gestion des exceptions
                System.err.println("Erreur lors du traitement du paiement ID : " + paiementRecurrentId);
                e.printStackTrace();
            }
        }
    }

    public List<Long> getPaymentsFromQueue() {
        LocalDate currentDate = LocalDate.now();
        return repository.findIdsForProcessing(currentDate);
    }


    private CarteVirtuelleDTO findCardByCurrency(List<CarteVirtuelleDTO> cartes, Devise currency) {
        // Look for a card with the correct currency
        return cartes.stream()
                .filter(carte -> carte.getDevise().equals(currency))
                .findFirst()
                .orElse(null);
    }


}
