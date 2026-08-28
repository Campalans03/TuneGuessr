package com.campalans.tuneguessr.domain.service;

import java.text.Normalizer;

public class GuessMatcher {
    public boolean isGuessCorrect(String guess, String answer) {
        return removeAccents(guess.trim())
                .equalsIgnoreCase(removeAccents(answer.trim()));
    }

    private String removeAccents(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }
}
