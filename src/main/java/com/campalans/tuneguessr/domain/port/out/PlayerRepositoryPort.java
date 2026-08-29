package com.campalans.tuneguessr.domain.port.out;

import com.campalans.tuneguessr.domain.model.Player;

import java.util.Optional;
import java.util.UUID;

public interface PlayerRepositoryPort {
    Optional<Player> findById(UUID playerId);
    Player save(Player player);
}
