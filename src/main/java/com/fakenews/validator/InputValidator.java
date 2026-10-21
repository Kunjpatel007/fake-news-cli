package com.fakenews.validator;

public class InputValidator {
    private InputValidator() {
    }

    public static String validateNewsText(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null.");
        }
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty. Please provide a news headline, message, or WhatsApp forward.");
        }
        return trimmed;
    }
}
