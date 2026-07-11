package com.example.glpi.service;

import com.example.glpi.dto.LangueValeurDto;
import com.example.glpi.dto.StatusResponseDto;
import com.example.glpi.dto.UpdateStatutValeurRequest;
import com.example.glpi.entity.Langue;
import com.example.glpi.entity.LangueStatut;
import com.example.glpi.entity.Status;
import com.example.glpi.repository.LangueRepository;
import com.example.glpi.repository.LangueStatutRepository;
import com.example.glpi.repository.StatusRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StatusService {

    private final StatusRepository statusRepository;

    private final LangueRepository langueRepository;

    private final LangueStatutRepository langueStatutRepository;

    public List<StatusResponseDto> getAllStatusWithLangues() {
        List<Status> statuses = statusRepository.findAll();
        List<StatusResponseDto> response = new ArrayList<>();

        for (Status status : statuses) {
            List<LangueValeurDto> langues = new ArrayList<>();

            if (status.getLangueStatuts() != null) {
                for (LangueStatut ls : status.getLangueStatuts()) {
                    langues.add(new LangueValeurDto(ls.getLangue().getCode(), ls.getValue()));
                }
            }

            response.add(new StatusResponseDto(status.getId(), status.getCouleur(), langues));
        }

        return response;
    }

    public StatusResponseDto updateStatutValeur(UpdateStatutValeurRequest request) {
        // Vérifier que le statut existe
        Optional<Status> statusOpt = statusRepository.findById(request.getId());
        if (!statusOpt.isPresent()) {
            throw new RuntimeException("Statut avec l'id " + request.getId() + " n'existe pas");
        }

        Status status = statusOpt.get();

        // Chercher ou créer la langue
        Optional<Langue> langueOpt = langueRepository.findByCode(request.getLangue());
        Langue langue;
        if (!langueOpt.isPresent()) {
            langue = new Langue(request.getLangue());
            langue = langueRepository.save(langue);
        } else {
            langue = langueOpt.get();
        }

        // Chercher ou créer l'entrée langue_statut
        Optional<LangueStatut> langueStatutOpt = langueStatutRepository.findByStatusIdAndLangueCode(status.getId(), request.getLangue());

        LangueStatut langueStatut;
        if (langueStatutOpt.isPresent()) {
            langueStatut = langueStatutOpt.get();
            langueStatut.setValue(request.getValeur());
        } else {
            langueStatut = new LangueStatut(status, langue, request.getValeur());
        }

        langueStatutRepository.save(langueStatut);

        // Retourner l'objet statut mis à jour
        status = statusRepository.findById(request.getId()).get();
        List<LangueValeurDto> langues = new ArrayList<>();
        if (status.getLangueStatuts() != null) {
            for (LangueStatut ls : status.getLangueStatuts()) {
                langues.add(new LangueValeurDto(ls.getLangue().getCode(), ls.getValue()));
            }
        }

        return new StatusResponseDto(status.getId(), status.getCouleur(), langues);
    }

    public StatusResponseDto updateStatusCouleur(Integer id, String couleur) {
        Optional<Status> statusOpt = statusRepository.findById(id);
        if (!statusOpt.isPresent()) {
            throw new RuntimeException("Statut avec l'id " + id + " n'existe pas");
        }

        Status status = statusOpt.get();
        status.setCouleur(couleur);
        statusRepository.save(status);

        // Préparer la réponse
        List<LangueValeurDto> langues = new ArrayList<>();
        if (status.getLangueStatuts() != null) {
            for (LangueStatut ls : status.getLangueStatuts()) {
                langues.add(new LangueValeurDto(ls.getLangue().getCode(), ls.getValue()));
            }
        }

        return new StatusResponseDto(status.getId(), status.getCouleur(), langues);
    }
}
