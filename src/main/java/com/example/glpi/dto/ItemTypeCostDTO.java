package com.example.glpi.dto;

import lombok.Data;

@Data
public class ItemTypeCostDTO {
    private String type;
    private Double superCost;
    private Double openCost;
}
