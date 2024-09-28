package com.example.clinic_management.enums;

public enum TimeSlot {
    SLOT_7_TO_8("7am to 8am"),
    SLOT_8_TO_9("8am to 9am"),
    SLOT_9_TO_10("9am to 10am"),

    SLOT_13_TO_14("1pm to 2pm"),

    SLOT_14_TO_15("2pm to 3pm"),

    SLOT_15_TO_16("3pm to 4pm");

    private final String slot;

    TimeSlot(String slot) {
        this.slot = slot;
    }

    public String getSlot() {
        return this.slot;
    }
}
