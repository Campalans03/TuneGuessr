package com.campalans.tuneguessr.domain.service;

public class ScoringPolicy {
    
    public int calculateScore(int attempts) {
        return switch (attempts) {
            case 0 -> 100;
            case 1 -> 80;
            case 2 -> 60;
            case 3 -> 40;
            case 4 -> 20;
            default -> 0;
        };
    }
}
