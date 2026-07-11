package com.example.glpi.controller;

import com.example.glpi.dto.ApiResponse;
import com.example.glpi.entity.Langue;
import com.example.glpi.service.LangueService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/langues")
public class LangueRestController {

    private final LangueService langueService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Langue>>> listAllLangues() {
        try {
            List<Langue> langues = langueService.getAllLangues();
            return ResponseEntity.ok(ApiResponse.success(langues));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }
}
