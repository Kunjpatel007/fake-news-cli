package com.fakenews.detection;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternDetectionStrategy extends NewsDetectionStrategy {
    private static final Pattern EXCLAMATION_PATTERN = Pattern.compile("!{2,}");
    private static final Pattern ALL_CAPS_PATTERN = Pattern.compile("\\b[A-Z]{4,}\\b");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\b\\d+%\\b");
    private static final Pattern URL_PATTERN = Pattern.compile("(http|https)://\\S+|www\\.\\S+");

    @Override
    public double calculateConfidence(String text) {
        clearIndicators();
        if (text == null || text.isEmpty()) {
            return 0.5;
        }
        double score = 0.0;
        if (EXCLAMATION_PATTERN.matcher(text).find()) {
            score += 0.15;
            addIndicator("Excessive exclamation marks");
        }
        if (ALL_CAPS_PATTERN.matcher(text).find()) {
            score += 0.1;
            addIndicator("All-caps words");
        }
        if (NUMBER_PATTERN.matcher(text).find()) {
            score += 0.1;
            addIndicator("Percent/statistic claims");
        }
        if (URL_PATTERN.matcher(text).find()) {
            score += 0.1;
            addIndicator("Contains URL/link");
        }
        double finalScore = 0.4 + Math.min(score, 0.4);
        return Math.min(finalScore, 0.9);
    }

    @Override
    public String getPrediction(double confidenceScore) {
        if (confidenceScore >= 0.6) {
            return "Likely Suspicious";
        } else if (confidenceScore <= 0.4) {
            return "Likely Reliable";
        } else {
            return "Uncertain";
        }
    }
}
