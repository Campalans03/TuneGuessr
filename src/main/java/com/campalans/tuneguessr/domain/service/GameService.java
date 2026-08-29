package com.campalans.tuneguessr.domain.service;

import com.campalans.tuneguessr.domain.model.GameSession;
import com.campalans.tuneguessr.domain.model.Player;
import com.campalans.tuneguessr.domain.model.Song;
import com.campalans.tuneguessr.domain.port.in.StartGameUseCase;
import com.campalans.tuneguessr.domain.port.out.GameSessionRepositoryPort;
import com.campalans.tuneguessr.domain.port.out.PlayerRepositoryPort;
import com.campalans.tuneguessr.domain.port.out.SongCatalogPort;

import java.util.UUID;

public class GameService implements StartGameUseCase {

    private final SongCatalogPort songCatalogPort;
    private final PlayerRepositoryPort playerRepositoryPort;
    private final GameSessionRepositoryPort gameSessionRepositoryPort;
    private final GuessMatcher guessMatcher;
    private final ScoringPolicy scoringPolicy;

    public GameService(SongCatalogPort songCatalogPort,
                       PlayerRepositoryPort playerRepositoryPort,
                       GameSessionRepositoryPort gameSessionRepositoryPort,
                       GuessMatcher guessMatcher,
                       ScoringPolicy scoringPolicy) {
        this.songCatalogPort = songCatalogPort;
        this.playerRepositoryPort = playerRepositoryPort;
        this.gameSessionRepositoryPort = gameSessionRepositoryPort;
        this.guessMatcher = guessMatcher;
        this.scoringPolicy = scoringPolicy;
    }

    public GameSession startGame(UUID playerId) {
        Player player = playerRepositoryPort.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found: " + playerId));

        Song song = songCatalogPort.findRandomSongExcluding(player.getPlayedSongIds());

        GameSession session = GameSession.builder()
                .id(UUID.randomUUID())
                .song(song)
                .guessMatcher(guessMatcher)
                .scoringPolicy(scoringPolicy)
                .build();

        return gameSessionRepositoryPort.save(session);
    }
}