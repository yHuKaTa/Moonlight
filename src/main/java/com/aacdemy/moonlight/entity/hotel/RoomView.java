package com.aacdemy.moonlight.entity.hotel;

public enum RoomView {
    SEA("Sea"),
    GARDEN("Garden"),
    POOL("Pool");
    private final String name;

    RoomView(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
