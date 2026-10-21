package com.fakenews.detection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class KeywordDetectionStrategy extends NewsDetectionStrategy {
    private static final Set<String> SUSPICIOUS_KEYWORDS = new HashSet<>(Arrays.asList(
            "shocking", "unbelievable", "miracle", "cure", "breaking", "exclusive", "secret",
            "leaked", "hidden", "banned", "whatsapp forward", "viral", "click here", "free",
            "won lottery", "claim", "urgent", "scam alert", "fake", "hoax"
    ));

    @Override
    public double calculateConfidence(String text) {
        clearIndicators();
        if (text == null || text.isEmpty()) {
            return 0.5;
        }
        String lowerText = text.toLowerCase(Locale.ROOT);
        int count = 0;
        for (String keyword : SUSPICIOUS_KEYWORDS) {
            if (lowerText.contains(keyword)) {
                count++;
                addIndicator("Suspicious keyword: '" + keyword + "'");
            }
        }
        double base = 0.3;
        double extra = Math.min(count * 0.15, 0.4);
        return Math.min(base + extra, 0.9);
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
