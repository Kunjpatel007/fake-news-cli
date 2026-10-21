package com.fakenews.repository;

import com.fakenews.model.NewsSearch;

import java.util.List;

public interface NewsRepository {
    void save(NewsSearch newsSearch);

    List<NewsSearch> findAll();

    List<NewsSearch> findByKeyword(String keyword);

    long countAll();

    long countByPrediction(String prediction);

    void deleteAll();
}
