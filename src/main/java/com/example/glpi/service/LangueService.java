package com.example.glpi.service;

import com.example.glpi.entity.Langue;
import com.example.glpi.repository.LangueRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LangueService {

    private final LangueRepository langueRepository;

    public List<Langue> getAllLangues() {
        return langueRepository.findAll();
    }
}
