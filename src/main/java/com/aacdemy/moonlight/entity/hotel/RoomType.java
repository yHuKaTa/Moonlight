package com.aacdemy.moonlight.entity.hotel;

public enum RoomType {
    STANDARD("24", "Standard"),
    STUDIO("34", "Studio"),
    APARTMENT("56", "Apartment");

    private final String area;
    private final String name;

    RoomType(String area, String name) {
        this.area = area;
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public String getName() {
        return name;
    }
}
