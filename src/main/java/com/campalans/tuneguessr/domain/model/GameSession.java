package com.campalans.tuneguessr.domain.model;

import com.campalans.tuneguessr.domain.service.GuessMatcher;
import com.campalans.tuneguessr.domain.service.ScoringPolicy;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class GameSession {

    private final UUID id;
    private final Song song;
    private final GuessMatcher guessMatcher;
    private final ScoringPolicy scoringPolicy;

    @Builder.Default
    private final List<Attempt> attempts = new ArrayList<>();
    @Builder.Default
    private int currentSnippetSeconds = 1;
    @Builder.Default
    private RoundStatus roundStatus = RoundStatus.PLAYING;

    public GuessResult guess(String guessText) {
        boolean correct = guessMatcher.isGuessCorrect(guessText, song.title());

        if (correct) {
            roundStatus = RoundStatus.WON;
            return new GuessResult(roundStatus, scoringPolicy.calculateScore(attempts.size()), song.title());
        }

        throw new UnsupportedOperationException("TODO: fallo");
    }
}