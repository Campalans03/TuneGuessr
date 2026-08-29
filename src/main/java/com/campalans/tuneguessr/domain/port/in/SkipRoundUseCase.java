package com.campalans.tuneguessr.domain.port.in;

import com.campalans.tuneguessr.domain.model.GuessResult;

import java.util.UUID;

public interface SkipRoundUseCase {
    GuessResult skipRound(UUID sessionId);
}
