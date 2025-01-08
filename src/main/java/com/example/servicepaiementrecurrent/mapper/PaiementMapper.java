package com.example.servicepaiementrecurrent.mapper;


import com.example.servicepaiementrecurrent.dto.PaiementRecurrentDTO;
import com.example.servicepaiementrecurrent.entities.PaiementRecurrent;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.Mapping;

@Component("PaiementMapper")
public class PaiementMapper {
    private static final ModelMapper modelMapper = new ModelMapper();
    // Custom mapping: Only map utilisateur.id to utilisateurId in DTO


    // Map Transaction to TransactionDTO
    public PaiementRecurrentDTO toDTO(PaiementRecurrent paiementRecurrent) {
        return modelMapper.map(paiementRecurrent, PaiementRecurrentDTO.class);
    }

    // Map TransactionDTO to Transaction
    public  PaiementRecurrent toEntity(PaiementRecurrentDTO paiementRecurrentDTO) {
        return modelMapper.map(paiementRecurrentDTO, PaiementRecurrent.class);
    }
}
