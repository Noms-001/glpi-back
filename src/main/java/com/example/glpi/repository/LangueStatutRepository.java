package com.example.glpi.repository;

import com.example.glpi.entity.LangueStatut;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LangueStatutRepository extends JpaRepository<LangueStatut, Integer> {
    Optional<LangueStatut> findByStatusIdAndLangueCode(Integer statusId, String languageCode);
}
