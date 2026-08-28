package com.campalans.tuneguessr.domain.model;

public record GuessResult(RoundStatus roundStatus, int score, String correctAnswerTitle) {
}