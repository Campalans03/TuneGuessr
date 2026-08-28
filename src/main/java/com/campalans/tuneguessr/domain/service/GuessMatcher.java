package com.campalans.tuneguessr.domain.service;

import java.text.Normalizer;
import java.util.regex.Pattern;

/// This class is responsible for matching user guesses against the correct answer,
/// taking into account various factors such as case insensitivity, whitespace normalization,
/// and ignoring certain characters like parentheses and accents that may be given by the API.
public class GuessMatcher {

    private static final Pattern PARENTHESIS = Pattern.compile("\\([^)]*\\)|\\[[^]]*]");
    private static final Pattern MULTI_SPACE = Pattern.compile("\\s+");

    public boolean isGuessCorrect(String guess, String answer) {
        return normalize(guess).equalsIgnoreCase(normalize(answer));
    }

    private String normalize(String text) {
        String withoutParenthesis = PARENTHESIS.matcher(text).replaceAll("");
        String withoutAccents = removeAccents(withoutParenthesis);
        return MULTI_SPACE.matcher(withoutAccents.trim()).replaceAll(" ");
    }

    private String removeAccents(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }
}
