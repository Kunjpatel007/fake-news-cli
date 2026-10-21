package com.fakenews.model;

import java.sql.Timestamp;

public class NewsSearch {
    private int id;
    private String newsText;
    private String prediction;
    private double confidenceScore;
    private String explanation;
    private Timestamp timestamp;

    public NewsSearch() {
    }

    public NewsSearch(String newsText, String prediction, double confidenceScore, String explanation, Timestamp timestamp) {
        this.newsText = newsText;
        this.prediction = prediction;
        this.confidenceScore = confidenceScore;
        this.explanation = explanation;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNewsText() {
        return newsText;
    }

    public void setNewsText(String newsText) {
        this.newsText = newsText;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Prediction: %s | Confidence: %.2f%% | Time: %s\nText: %s\nExplanation: %s\n",
                id, prediction, confidenceScore * 100, timestamp, newsText, explanation);
    }
}
