package com.example.glpi.enums;

public enum CostTypeEnum {

    SUPER_COST(1),
    OPEN_COST(2);

    private final Integer id;

    CostTypeEnum(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}