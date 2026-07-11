package com.example.glpi.dto;

public class TicketCostDTO {
    private Integer ticketId;
    private Integer groupId;
    private Integer costTypeId;
    private Double cost;
    private Double value;
    private Integer mode;

    public TicketCostDTO(Integer ticketId, Integer groupId, Integer costTypeId, Double cost, Double value, Integer mode) {
        this.ticketId = ticketId;
        this.groupId = groupId;
        this.costTypeId = costTypeId;
        this.cost = cost;
        this.value = value;
        this.mode = mode;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

        public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Integer getCostTypeId() {
        return costTypeId;
    }

    public void setCostTypeId(Integer costTypeId) {
        this.costTypeId = costTypeId;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }
}
