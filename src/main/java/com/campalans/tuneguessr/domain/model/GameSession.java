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
    private final UUID playerId;
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

    /// This method is used to process a guess made by the player.
    /// It checks if the guess is correct, updates the round status,
    /// and returns a GuessResult object containing the outcome of the guess.
    public GuessResult guess(String guessText) {
        boolean correct = guessMatcher.isGuessCorrect(guessText, song.title());

        if (correct) {
            roundStatus = RoundStatus.WON;
            return new GuessResult(roundStatus, scoringPolicy.calculateScore(attempts.size()), song.title());
        }

        return recordFailedAttempt(guessText);
    }

    /// This method is used to skip the current snippet and move to the next one.
    /// If the player has already used all available snippets, it sets the round status to LOST.
    public GuessResult skip() {
        return recordFailedAttempt(null);
    }

    public GuessResult giveUp() {
        roundStatus = RoundStatus.LOST;
        return new GuessResult(roundStatus, 0, song.title());
    }

    private GuessResult recordFailedAttempt(String guessText) {
        attempts.add(new Attempt(currentSnippetSeconds, guessText, false));

        if (attempts.size() == SNIPPET_SECONDS.length) {
            roundStatus = RoundStatus.LOST;
            return new GuessResult(roundStatus, 0, song.title());
        }

        currentSnippetSeconds = SNIPPET_SECONDS[attempts.size()];
        return new GuessResult(roundStatus, 0, null);
    }
}