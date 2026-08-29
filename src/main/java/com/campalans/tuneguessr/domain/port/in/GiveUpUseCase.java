package com.campalans.tuneguessr.domain.port.in;

import com.campalans.tuneguessr.domain.model.GuessResult;

import java.util.UUID;

public interface GiveUpUseCase {
    GuessResult giveUp(UUID sessionId);
}
