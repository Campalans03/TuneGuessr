package com.campalans.tuneguessr.adapter.out.memory;

import com.campalans.tuneguessr.domain.model.GameSession;
import com.campalans.tuneguessr.domain.port.out.GameSessionRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryGameSessionRepository implements GameSessionRepositoryPort {

    private final Map<UUID, GameSession> sessions = new ConcurrentHashMap<>();

    @Override
    public GameSession save(GameSession gameSession) {
        sessions.put(gameSession.getId(), gameSession);
        return gameSession;
    }

    @Override
    public Optional<GameSession> findById(UUID sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }
}
