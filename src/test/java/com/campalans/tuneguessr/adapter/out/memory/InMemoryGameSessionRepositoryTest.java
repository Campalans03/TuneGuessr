package com.campalans.tuneguessr.adapter.out.memory;

import com.campalans.tuneguessr.domain.model.GameSession;
import com.campalans.tuneguessr.domain.model.Song;
import com.campalans.tuneguessr.domain.service.GuessMatcher;
import com.campalans.tuneguessr.domain.service.ScoringPolicy;
import org.junit.jupiter.api.Test;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryGameSessionRepositoryTest {

    private final InMemoryGameSessionRepository repository = new InMemoryGameSessionRepository();

    @Test
    void save_and_find_session_by_id() {
        GameSession session = GameSession.builder()
                .id(UUID.randomUUID())
                .playerId(UUID.randomUUID())
                .song(new Song("1", "Song Title", "Artist Name", "http://example.com/preview.mp3"))
                .guessMatcher(new GuessMatcher())
                .scoringPolicy(new ScoringPolicy())
                .build();

        repository.save(session);

        Optional<GameSession> retrievedSession = repository.findById(session.getId());

        assertTrue(retrievedSession.isPresent());
        assertEquals(session.getId(), retrievedSession.get().getId());
    }

    @Test
    void finding_unknow_id_returns_empty() {
        Optional<GameSession> retrievedSession = repository.findById(UUID.randomUUID());

        assertTrue(retrievedSession.isEmpty());
    }
}
