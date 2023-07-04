package com.aacdemy.moonlight.entity.car.enums;

public enum CarType {
    SPORT("Sport"),
    SEDAN("Sedan"),
    VAN("Van");

    private final String carType;

    CarType(String carType) {
        this.carType = carType;
    }

    public String getCarType() {
        return carType;
    }
}
