package com.aacdemy.moonlight.entity.hotel;

public enum RoomBedType {
    TWIN_BEDS("Twin beds"),
    BEDROOM("Bedroom");

    public final String label;

    private RoomBedType(String label) {
        this.label = label;
    }
}
