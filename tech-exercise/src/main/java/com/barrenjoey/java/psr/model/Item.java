package com.barrenjoey.java.psr.model;

import java.util.Arrays;

public enum Item {

    Rock("R"), Paper("P"), Scissor("S"), Lizard("L");
    private final String code;
    Item(String code) {
        this.code = code;
    }

    public static Item fromString(String item) {
        return Arrays.stream(Item.values()).filter(val -> val.code.equals(item)).findFirst().orElseThrow(() -> {
            throw new RuntimeException("Cannot find the correct move");
        });
    }
}
