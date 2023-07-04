package com.aacdemy.moonlight.entity;

public enum PaymentStatus {
    PAID("Paid"),
    UNPAID("Unpaid");
    public final String label;

     PaymentStatus(String label) {
        this.label = label;
    }
}
