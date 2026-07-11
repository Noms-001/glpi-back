package com.example.glpi.dto;

public class ItemGroup {
    private Integer itemId;
    private Double cost;

    public ItemGroup(Integer itemId, Number cost) {
        this.itemId = itemId;
        this.cost = cost.doubleValue();
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }
}
