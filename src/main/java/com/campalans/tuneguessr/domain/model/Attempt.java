package com.campalans.tuneguessr.domain.model;

public record Attempt(int snippetSeconds, String guessText, boolean isCorrect) {
}