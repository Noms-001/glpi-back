package com.example.glpi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.glpi.entity.Plafond;

public interface PlafondRepository extends JpaRepository<Plafond, Integer> {
    
}
