package com.example.glpi.controller;

import com.example.glpi.dto.ApiResponse;
import com.example.glpi.dto.StatusResponseDto;
import com.example.glpi.dto.UpdateStatutValeurRequest;
import com.example.glpi.dto.UpdateStatusCouleurRequest;
import com.example.glpi.service.StatusService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/status")
public class StatusRestController {

    private final StatusService statusService;

    /**
     * GET /api/status
     * Liste tous les statuts avec leurs langues et valeurs
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<StatusResponseDto>>> listAllStatus() {
        try {
            List<StatusResponseDto> statuses = statusService.getAllStatusWithLangues();
            return ResponseEntity.ok(ApiResponse.success(statuses));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * POST /api/status
     * Met à jour ou crée une valeur de statut pour une langue
     */
    @PostMapping
    public ResponseEntity<ApiResponse<StatusResponseDto>> updateStatutValeur(@RequestBody UpdateStatutValeurRequest request) {
        try {
            StatusResponseDto response = statusService.updateStatutValeur(request);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * POST /api/status/update-couleur
     * Met à jour la couleur d'un statut
     */
    @PostMapping("/color")
    public ResponseEntity<ApiResponse<StatusResponseDto>> updateCouleur(@RequestBody UpdateStatusCouleurRequest request) {
        try {
            StatusResponseDto response = statusService.updateStatusCouleur(request.getId(), request.getCouleur());
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }
}
