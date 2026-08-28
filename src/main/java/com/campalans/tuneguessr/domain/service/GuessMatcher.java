package com.campalans.tuneguessr.domain.service;

import java.text.Normalizer;
import java.util.regex.Pattern;

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
