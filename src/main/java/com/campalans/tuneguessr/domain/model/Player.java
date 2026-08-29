package com.campalans.tuneguessr.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Builder
public class Player {
    private final UUID id;
    private final String name;

    @Builder.Default
    private int totalScore = 0;
    @Builder.Default
    private final Set<String> playedSongIds = new HashSet<>();

    public void recordScore(int score) {
        totalScore += score;
    }

    public void markSongAsPlayed(String songId) {
        playedSongIds.add(songId);
    }
}
