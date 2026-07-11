package com.example.glpi.enums;

public enum ModeEnum {

    LAST_SUPER_COST(1),
    FIRST_SUPER_COST(2),
    AVG_SUPER_COST(3),
    SUM_SUPER_COST(4);

    private final Integer id;

    ModeEnum(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
