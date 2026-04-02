package com.resumeaianalyzer.resumeaianalyzer.bean;

import java.util.Arrays;

public enum MatchType {

    STRONG_MATCH(80, 100, "Strong Match", "Top 5% candidate"),
    GOOD_MATCH(65, 79, "Good Match", "Highly Suitable"),
    MODERATE_MATCH(50, 64, "Moderate Match", "Average Fit"),
    WEAK_MATCH(30, 49, "Weak Match", "Low Fit"),
    NO_MATCH(0, 29, "No Match", "Not Suitable");

    private final int min;
    private final int max;
    private final String label;
    private final String description;

    MatchType(int min, int max, String label, String description) {
        this.min = min;
        this.max = max;
        this.label = label;
        this.description = description;
    }

    public static MatchType fromScore(int score) {
        return Arrays.stream(values())
                .filter(type -> score >= type.min && score <= type.max)
                .findFirst()
                .orElse(NO_MATCH);
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }
}
