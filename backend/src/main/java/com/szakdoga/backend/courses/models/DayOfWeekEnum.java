package com.szakdoga.backend.courses.models;

// Enum to represent the days of the week
public enum DayOfWeekEnum {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    private final String dayName;

    DayOfWeekEnum(String dayName) {
        this.dayName = dayName;
    }

    public String getDayName() {
        return dayName;
    }

    public static DayOfWeekEnum fromString(String dayName) {
        for (DayOfWeekEnum day : DayOfWeekEnum.values()) {
            if (day.dayName.equalsIgnoreCase(dayName)) {
                return day;
            }
        }
        throw new IllegalArgumentException("No enum constant for day: " + dayName);
    }
}
