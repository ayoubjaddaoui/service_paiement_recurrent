package com.example.servicepaiementrecurrent.repository;


import com.example.servicepaiementrecurrent.entities.PaiementRecurrent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PaiementRecurrentRepository extends JpaRepository<PaiementRecurrent, Long> {
    List<PaiementRecurrent> findByStatus(String active);
    @Query("SELECT p.id FROM PaiementRecurrent p WHERE p.status = 'ACTIVE' AND p.nextExecutionDate = :currentDate")
    List<Long> findIdsForProcessing(@Param("currentDate") LocalDate currentDate);
}
