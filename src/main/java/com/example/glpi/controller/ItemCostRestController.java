package com.example.glpi.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.glpi.dto.ApiResponse;
import com.example.glpi.dto.CostItemDTO;
import com.example.glpi.dto.ItemCostDTO;
import com.example.glpi.dto.ItemTypeCostDTO;
import com.example.glpi.dto.TicketCostDTO;
import com.example.glpi.service.ItemCostService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/item-cost")
public class ItemCostRestController {

    private final ItemCostService itemCostService;
    
    @DeleteMapping("/reset")
    public ResponseEntity<ApiResponse<String>> reset() {
        try {
            itemCostService.reset();
            return ResponseEntity.ok(ApiResponse.success("Reset success"));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/ticket-cost")
    public ResponseEntity<ApiResponse<List<TicketCostDTO>>> getTicketCosts() {
        try {
            List<TicketCostDTO> response = itemCostService.getTicketCostDTOs();
            return ResponseEntity.ok(ApiResponse.success(response));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/ticket-cancel-cost")
    public ResponseEntity<ApiResponse<List<TicketCostDTO>>> getTicketCancelCosts() {
        try {
            List<TicketCostDTO> response = itemCostService.getTicketCancelCostDTOs();
            return ResponseEntity.ok(ApiResponse.success(response));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<CostItemDTO>>> getDetailsByItemType(@RequestParam String itemType) {
        try {
            List<CostItemDTO> response = itemCostService.getDetailsByItemType(itemType);
            return ResponseEntity.ok(ApiResponse.success(response));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }


    @GetMapping("/sum")
    public ResponseEntity<ApiResponse<Map<String, ItemTypeCostDTO>>> getSumCostByItemType() {
        try {
            Map<String, ItemTypeCostDTO> response = itemCostService.getCostDTOs();
            return ResponseEntity.ok(ApiResponse.success(response));
 
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<ApiResponse<String>> delete(@RequestParam Integer ticketId) {
        try {
            itemCostService.cancelLastSuperCost(ticketId);
            return ResponseEntity.ok(ApiResponse.success("Cancel SuperCost Success"));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/retablir")
    public ResponseEntity<ApiResponse<String>> retablir(@RequestParam Integer ticketId, @RequestParam Integer groupId) {
        try {
            itemCostService.retablirLastSuperCost(ticketId, groupId);
            return ResponseEntity.ok(ApiResponse.success("Retablir SuperCost Success"));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/plafond")
    public ResponseEntity<ApiResponse<String>> postPlafond(@RequestParam Double plafond) {
        try {
            itemCostService.setPlafond(plafond);
            return ResponseEntity.ok(ApiResponse.success("Retablir SuperCost Success"));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/new")
    public ResponseEntity<ApiResponse<String>> save(@RequestBody List<ItemCostDTO> itemCostDTOs) {
        try {
            itemCostService.saveItemCost(itemCostDTOs);
            return ResponseEntity.ok(ApiResponse.success("Save cost success"));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }


    @PostMapping("/update")
    public ResponseEntity<ApiResponse<String>> update(@RequestBody TicketCostDTO ticketCostDTO) {
        try {
            itemCostService.updateTicketCost(ticketCostDTO);;
            return ResponseEntity.ok(ApiResponse.success("Update cost success"));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }
    
}
