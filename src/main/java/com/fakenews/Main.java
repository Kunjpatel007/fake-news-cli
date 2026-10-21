package com.fakenews;

import com.fakenews.detection.NewsDetector;
import com.fakenews.model.NewsSearch;
import com.fakenews.repository.NewsRepository;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("Fake News & Rumour Detector CLI");
        System.out.println("==========================================");
        System.out.println("Note: This is an ESTIMATE based on heuristics.");
        System.out.println("A trained ML model or trusted fact-checking API can replace it later.");
        System.out.println("==========================================\n");

        com.fakenews.detection.NewsDetector detector = new com.fakenews.detection.SimpleNewsDetector();
        NewsRepository repository = new NewsDAO();
        NewsService service = new NewsService(detector, repository);

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    checkNews(service);
                    break;
                case "2":
                    viewPreviousSearches(service);
                    break;
                case "3":
                    searchByKeyword(service);
                    break;
                case "4":
                    viewStatistics(service);
                    break;
                case "5":
                    deleteSearchHistory(service);
                    break;
                case "6":
                    running = false;
                    System.out.println("Thank you for using Fake News & Rumour Detector. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.\n");
                    break;
            }
        }
        scanner.close();
        DatabaseConfig.close();
    }

    private static void printMenu() {
        System.out.println("1. Check News or Rumour");
        System.out.println("2. View Previous Searches");
        System.out.println("3. Search News by Keyword");
        System.out.println("4. View Statistics");
        System.out.println("5. Delete Search History");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void checkNews(NewsService service) {
        System.out.print("Enter news headline/message/WhatsApp forward: ");
        String input = scanner.nextLine();
        try {
            NewsSearch result = service.checkNews(input);
            System.out.println("\n=== Detection Result ===");
            System.out.println("Prediction: " + result.getPrediction());
            System.out.printf("Confidence Score: %.2f%%\n", result.getConfidenceScore() * 100);
            System.out.println("Explanation: " + result.getExplanation());
            System.out.println("Timestamp: " + result.getTimestamp());
            System.out.println("========================\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void viewPreviousSearches(NewsService service) {
        List<NewsSearch> searches = service.getAllSearches();
        if (searches.isEmpty()) {
            System.out.println("No previous searches found.\n");
            return;
        }
        System.out.println("\n=== Previous Searches ===");
        for (NewsSearch search : searches) {
            System.out.println(search);
        }
        System.out.println("=========================\n");
    }

    private static void searchByKeyword(NewsService service) {
        System.out.print("Enter keyword to search: ");
        String keyword = scanner.nextLine();
        try {
            List<NewsSearch> searches = service.searchByKeyword(keyword);
            if (searches.isEmpty()) {
                System.out.println("No matches found for keyword: " + keyword + "\n");
                return;
            }
            System.out.println("\n=== Search Results ===");
            for (NewsSearch search : searches) {
                System.out.println(search);
            }
            System.out.println("=======================\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void viewStatistics(NewsService service) {
        long total = service.getTotalSearches();
        long suspicious = service.getSearchCountByPrediction("Likely Suspicious");
        long reliable = service.getSearchCountByPrediction("Likely Reliable");
        long uncertain = service.getSearchCountByPrediction("Uncertain");
        System.out.println("\n=== Statistics ===");
        System.out.println("Total Searches: " + total);
        System.out.println("Likely Suspicious: " + suspicious);
        System.out.println("Likely Reliable: " + reliable);
        System.out.println("Uncertain: " + uncertain);
        System.out.println("===================\n");
    }

    private static void deleteSearchHistory(NewsService service) {
        System.out.print("Are you sure you want to delete all search history? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (confirm.equals("y") || confirm.equals("yes")) {
            service.deleteSearchHistory();
            System.out.println("Search history deleted successfully.\n");
        } else {
            System.out.println("Deletion cancelled.\n");
        }
    }
}





