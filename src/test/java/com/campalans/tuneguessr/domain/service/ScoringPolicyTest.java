package com.campalans.tuneguessr.domain.service;

import com.campalans.tuneguessr.domain.service.ScoringPolicy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoringPolicyTest {

    private final ScoringPolicy scoringPolicy = new ScoringPolicy();

    @Test
    void scoring_in_first_try_give_100_points() {
        assertEquals(100, scoringPolicy.calculateScore(0));
    }

    @Test
    void scoring_every_try_give_less_points() {
        assertEquals(80, scoringPolicy.calculateScore(1));
        assertEquals(60, scoringPolicy.calculateScore(2));
        assertEquals(40, scoringPolicy.calculateScore(3));
        assertEquals(20, scoringPolicy.calculateScore(4));
    }
}
