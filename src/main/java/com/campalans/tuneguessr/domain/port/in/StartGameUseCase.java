package com.campalans.tuneguessr.domain.port.in;

import com.campalans.tuneguessr.domain.model.GameSession;

import java.util.UUID;

public interface StartGameUseCase {
    GameSession startGame(UUID playerId);
}
