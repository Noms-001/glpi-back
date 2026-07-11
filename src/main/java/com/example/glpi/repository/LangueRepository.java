package com.example.glpi.repository;

import com.example.glpi.entity.Langue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LangueRepository extends JpaRepository<Langue, Integer> {
    Optional<Langue> findByCode(String code);
}
