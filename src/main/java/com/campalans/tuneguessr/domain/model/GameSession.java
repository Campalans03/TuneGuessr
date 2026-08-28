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

    private static final int[] SNIPPET_SECONDS = {1, 2, 4, 8, 10};

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

        attempts.add(new Attempt(currentSnippetSeconds, guessText, false));

        if(attempts.size() == SNIPPET_SECONDS.length) {
            roundStatus = RoundStatus.LOST;
            return new GuessResult(roundStatus, 0, song.title());
        }

        currentSnippetSeconds = SNIPPET_SECONDS[attempts.size()];

        return new GuessResult(roundStatus, 0, null);
    }

    public void skip() {
        if(attempts.size() < SNIPPET_SECONDS.length - 1) {
            attempts.add(new Attempt(currentSnippetSeconds, null, false));
            currentSnippetSeconds = SNIPPET_SECONDS[attempts.size()];
        } else {
            roundStatus = RoundStatus.LOST;
        }
    }
}