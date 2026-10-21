package com.fakenews;

import com.fakenews.detection.NewsDetector;
import com.fakenews.model.NewsSearch;
import com.fakenews.repository.NewsRepository;
import com.fakenews.validator.InputValidator;

import java.util.List;

public class NewsService {
    private final NewsDetector detector;
    private final NewsRepository repository;

    public NewsService(NewsDetector detector, NewsRepository repository) {
        this.detector = detector;
        this.repository = repository;
    }

    public NewsSearch checkNews(String newsText) {
        String validatedText = InputValidator.validateNewsText(newsText);
        NewsSearch result = detector.detect(validatedText);
        repository.save(result);
        return result;
    }

    public List<NewsSearch> getAllSearches() {
        return repository.findAll();
    }

    public List<NewsSearch> searchByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Keyword cannot be empty.");
        }
        return repository.findByKeyword(keyword.trim());
    }

    public long getTotalSearches() {
        return repository.countAll();
    }

    public long getSearchCountByPrediction(String prediction) {
        return repository.countByPrediction(prediction);
    }

    public void deleteSearchHistory() {
        repository.deleteAll();
    }
}
