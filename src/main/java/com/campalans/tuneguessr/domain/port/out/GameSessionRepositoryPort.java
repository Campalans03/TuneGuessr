package com.campalans.tuneguessr.domain.port.out;

import com.campalans.tuneguessr.domain.model.GameSession;

import java.util.Optional;
import java.util.UUID;

public interface GameSessionRepositoryPort {
    GameSession save(GameSession gameSession);
    Optional<GameSession> findById(UUID gameSessionId);
}
