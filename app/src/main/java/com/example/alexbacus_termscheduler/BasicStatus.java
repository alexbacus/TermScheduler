package com.example.alexbacus_termscheduler;

public enum BasicStatus {
    ACTIVE(1),
    TRASHED(2);

    private final int value;
    private BasicStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
