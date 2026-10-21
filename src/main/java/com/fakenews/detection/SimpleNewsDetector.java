package com.fakenews.detection;

import com.fakenews.model.NewsSearch;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SimpleNewsDetector implements NewsDetector {
    private final List<NewsDetectionStrategy> strategies;

    public SimpleNewsDetector() {
        this.strategies = new ArrayList<>();
        this.strategies.add(new KeywordDetectionStrategy());
        this.strategies.add(new PatternDetectionStrategy());
    }

    public SimpleNewsDetector(List<NewsDetectionStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public NewsSearch detect(String newsText) {
        if (newsText == null || newsText.trim().isEmpty()) {
            throw new IllegalArgumentException("News text cannot be empty");
        }
        double totalConfidence = 0.0;
        StringBuilder explanationBuilder = new StringBuilder();

        for (int i = 0; i < strategies.size(); i++) {
            NewsDetectionStrategy strategy = strategies.get(i);
            double conf = strategy.calculateConfidence(newsText);
            totalConfidence += conf;
            explanationBuilder.append(strategy.getExplanation());
            if (i < strategies.size() - 1) {
                explanationBuilder.append("\n");
            }
        }

        double avgConfidence = strategies.isEmpty() ? 0.5 : totalConfidence / strategies.size();
        String prediction = determinePrediction(avgConfidence);

        String combinedExplanation = explanationBuilder.toString();
        return new NewsSearch(newsText.trim(), prediction, avgConfidence, combinedExplanation, Timestamp.from(Instant.now()));
    }

    private String determinePrediction(double confidenceScore) {
        if (confidenceScore >= 0.6) {
            return "Likely Suspicious";
        } else if (confidenceScore <= 0.4) {
            return "Likely Reliable";
        } else {
            return "Uncertain";
        }
    }
}
