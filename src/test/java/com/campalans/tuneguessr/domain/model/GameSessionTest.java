package com.campalans.tuneguessr.domain.model;

import com.campalans.tuneguessr.domain.service.GuessMatcher;
import com.campalans.tuneguessr.domain.service.ScoringPolicy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameSessionTest {

    private final Song song = new Song("1", "Song Title", "Artist Name", "URL");

    @Test
    void new_session_let_you_listen_1_second_of_the_song() {
        GameSession session = newSession();
        assertEquals (RoundStatus.PLAYING, session.getRoundStatus());
        assertEquals (1, session.getCurrentSnippetSeconds());
    }

    @Test
    void guessing_the_song_in_first_try_give_100_points() {
        GameSession session = newSession();
        GuessResult result = session.guess("Song Title");
        assertEquals(RoundStatus.WON, result.roundStatus());
        assertEquals(100, result.score());
    }

    private GameSession newSession() {
        return GameSession.builder()
                .song(song)
                .guessMatcher(new GuessMatcher())
                .scoringPolicy(new ScoringPolicy())
                .currentSnippetSeconds(1)
                .build();
    }
}
