package com.fakenews;

import com.fakenews.model.NewsSearch;
import com.fakenews.repository.NewsRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NewsDAO implements NewsRepository {
    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS news_history (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "news_text TEXT NOT NULL," +
            "prediction VARCHAR(50) NOT NULL," +
            "confidence_score DOUBLE NOT NULL," +
            "explanation TEXT," +
            "timestamp DATETIME NOT NULL" +
            ")";

    private static final String INSERT_SQL = "INSERT INTO news_history (news_text, prediction, confidence_score, explanation, timestamp) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT id, news_text, prediction, confidence_score, explanation, timestamp FROM news_history ORDER BY id DESC";
    private static final String SELECT_BY_KEYWORD_SQL = "SELECT id, news_text, prediction, confidence_score, explanation, timestamp FROM news_history WHERE news_text LIKE ? ORDER BY id DESC";
    private static final String COUNT_ALL_SQL = "SELECT COUNT(*) FROM news_history";
    private static final String COUNT_BY_PREDICTION_SQL = "SELECT COUNT(*) FROM news_history WHERE prediction = ?";
    private static final String DELETE_ALL_SQL = "DELETE FROM news_history";

    public NewsDAO() {
        initializeTable();
    }

    private void initializeTable() {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_TABLE_SQL);
        } catch (SQLException e) {
            System.err.println("Error initializing table: " + e.getMessage());
        }
    }

    @Override
    public void save(NewsSearch newsSearch) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            ps.setString(1, newsSearch.getNewsText());
            ps.setString(2, newsSearch.getPrediction());
            ps.setDouble(3, newsSearch.getConfidenceScore());
            ps.setString(4, newsSearch.getExplanation());
            ps.setTimestamp(5, newsSearch.getTimestamp());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving news search: " + e.getMessage());
        }
    }

    @Override
    public List<NewsSearch> findAll() {
        List<NewsSearch> list = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                NewsSearch search = new NewsSearch();
                search.setId(rs.getInt("id"));
                search.setNewsText(rs.getString("news_text"));
                search.setPrediction(rs.getString("prediction"));
                search.setConfidenceScore(rs.getDouble("confidence_score"));
                search.setExplanation(rs.getString("explanation"));
                search.setTimestamp(rs.getTimestamp("timestamp"));
                list.add(search);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all searches: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<NewsSearch> findByKeyword(String keyword) {
        List<NewsSearch> list = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_KEYWORD_SQL)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NewsSearch search = new NewsSearch();
                    search.setId(rs.getInt("id"));
                    search.setNewsText(rs.getString("news_text"));
                    search.setPrediction(rs.getString("prediction"));
                    search.setConfidenceScore(rs.getDouble("confidence_score"));
                    search.setExplanation(rs.getString("explanation"));
                    search.setTimestamp(rs.getTimestamp("timestamp"));
                    list.add(search);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving searches by keyword: " + e.getMessage());
        }
        return list;
    }

    @Override
    public long countAll() {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(COUNT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting searches: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public long countByPrediction(String prediction) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(COUNT_BY_PREDICTION_SQL)) {
            ps.setString(1, prediction);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error counting by prediction: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public void deleteAll() {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_ALL_SQL)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting search history: " + e.getMessage());
        }
    }
}
