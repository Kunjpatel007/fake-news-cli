package com.fakenews.detection;

import java.util.ArrayList;
import java.util.List;

public abstract class NewsDetectionStrategy {
    protected final List<String> indicators = new ArrayList<>();

    public abstract double calculateConfidence(String text);

    public abstract String getPrediction(double confidenceScore);

    public String getExplanation() {
        if (indicators.isEmpty()) {
            return "No clear indicators found.";
        }
        StringBuilder sb = new StringBuilder("Indicators detected: ");
        sb.append(String.join(", ", indicators));
        sb.append(". Note: This is an estimate based on configurable heuristics and should not be treated as proof.");
        return sb.toString();
    }

    protected void clearIndicators() {
        indicators.clear();
    }

    protected void addIndicator(String indicator) {
        indicators.add(indicator);
    }
}
