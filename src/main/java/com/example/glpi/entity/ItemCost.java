package com.example.glpi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "item_cost")
public class ItemCost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "ticket_id", nullable = false)
    private Integer ticketId;

    @Column(name = "item_id", nullable = false)
    private Integer itemId;

    @Column(name = "item_type", nullable = false)
    private String itemType;

    @Column(name = "cost")
    private Double cost;

    @ManyToOne
    @JoinColumn(name = "cost_type_id")
    private CostType costType;

    @Column(name = "group_id") 
    private Integer groupId;

    @Column(name = "mode")
    private Integer mode;

    @Column(name = "value")
    private Double value;

    @Column(name = "isDelete")
    private boolean isDelete;
}
