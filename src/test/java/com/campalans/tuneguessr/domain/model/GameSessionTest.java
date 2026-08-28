package com.campalans.tuneguessr.domain.model;

import com.campalans.tuneguessr.domain.service.GuessMatcher;
import com.campalans.tuneguessr.domain.service.ScoringPolicy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    @Test
    void guessing_wrong_song_in_first_try_keeps_playing_with_more_snippet() {
        GameSession session = newSession();
        GuessResult result = session.guess("Wrong Song Title");

        assertEquals(RoundStatus.PLAYING, result.roundStatus());
        assertEquals(2, session.getCurrentSnippetSeconds());
        assertNull(result.correctAnswerTitle());
    }

    @Test
    void guessing_wrong_song_in_last_try_ends_the_game() {
        GameSession session = newSession();
        session.guess("Wrong Song Title");
        session.guess("Wrong Song Title");
        session.guess("Wrong Song Title");
        session.guess("Wrong Song Title");
        GuessResult result = session.guess("Wrong Song Title");

        assertEquals(RoundStatus.LOST, result.roundStatus());
        assertEquals(0, result.score());
        assertEquals("Song Title", result.correctAnswerTitle());
    }

    @Test
    void pressing_skip_button_leads_to_next_snippet() {
        GameSession session = newSession();
        session.skip();
        assertEquals(2, session.getCurrentSnippetSeconds());
    }

    @Test
    void pressing_skip_button_in_last_try_ends_the_game() {
        GameSession session = newSession();
        session.skip();
        session.skip();
        session.skip();
        session.skip();
        session.skip();
        assertEquals(RoundStatus.LOST, session.getRoundStatus());
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
