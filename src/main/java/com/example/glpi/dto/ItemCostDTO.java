package com.example.glpi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCostDTO {
    private Integer id;
    private Integer ticketId;
    private Integer itemId;
    private String itemType;
    private Double value;
    private String type;
    private Integer mode;
}
