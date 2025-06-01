package org.example.roomiehub.Enum;

import lombok.RequiredArgsConstructor;

public class Enums {

    @RequiredArgsConstructor
    public enum Role {
        USER,
        ADMIN;
    }

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public enum Hometown {
        HANOI, HCM, DANANG, HUE, OTHER
    }

    public enum Occupation {
        STUDENT, OFFICE_WORKER, FREELANCER, OTHER
    }

    public enum PriceRange {
        BELOW_3M, FROM_3M_TO_5M, FROM_5M_TO_7M, ABOVE_7M
    }

    public enum PreferredLocation {
        NEAR_UNIVERSITY, NEAR_BUSINESS_AREA, QUIET_AREA
    }

    public enum YesNo {
        YES, NO
    }

    public enum CookFrequency {
        NEVER, SOMETIMES, OFTEN
    }

    public enum SleepHabit {
        EARLY_SLEEPER, NIGHT_OWL, FLEXIBLE
    }
}