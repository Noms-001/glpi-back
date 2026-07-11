package com.example.glpi.dto;

public class CostItemDTO {
    private Integer itemId;
    private Double openCost;
    private Double superCost;

    public CostItemDTO(Integer itemId, Double openCost, Double superCost) {
        this.itemId = itemId;
        this.openCost = openCost;
        this.superCost = superCost;
    }

    public Integer getItemId() {
        return itemId;
    }

    public Double getOpenCost() {
        return openCost;
    }

    public Double getSuperCost() {
        return superCost;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public void setOpenCost(Double openCost) {
        this.openCost = openCost;
    }

    public void setSuperCost(Double superCost) {
        this.superCost = superCost;
    }

}
