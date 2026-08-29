package com.campalans.tuneguessr.domain.service;

import com.campalans.tuneguessr.domain.model.*;
import com.campalans.tuneguessr.domain.port.in.GiveUpUseCase;
import com.campalans.tuneguessr.domain.port.in.SkipRoundUseCase;
import com.campalans.tuneguessr.domain.port.in.StartGameUseCase;
import com.campalans.tuneguessr.domain.port.in.SubmitGuessUseCase;
import com.campalans.tuneguessr.domain.port.out.GameSessionRepositoryPort;
import com.campalans.tuneguessr.domain.port.out.PlayerRepositoryPort;
import com.campalans.tuneguessr.domain.port.out.SongCatalogPort;

import java.util.UUID;
import java.util.function.Function;

public class GameService implements StartGameUseCase, SubmitGuessUseCase, SkipRoundUseCase, GiveUpUseCase {

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

    @Override
    public GameSession startGame(UUID playerId) {
        Player player = playerRepositoryPort.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found: " + playerId));

        Song song = songCatalogPort.findRandomSongExcluding(player.getPlayedSongIds());

        GameSession session = GameSession.builder()
                .id(UUID.randomUUID())
                .playerId(playerId)
                .song(song)
                .guessMatcher(guessMatcher)
                .scoringPolicy(scoringPolicy)
                .build();

        return gameSessionRepositoryPort.save(session);
    }

    @Override
    public GuessResult submitGuess(UUID sessionId, String guessText) {
        return applyAction(sessionId, session -> session.guess(guessText));
    }

    @Override
    public GuessResult giveUp(UUID sessionId) {
        return applyAction(sessionId, GameSession::giveUp);
    }

    @Override
    public GuessResult skipRound(UUID sessionId) {
        return applyAction(sessionId, GameSession::skip);
    }

    private GuessResult applyAction(UUID sessionId, Function<GameSession, GuessResult> action) {
        GameSession session = gameSessionRepositoryPort.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Game session not found: " + sessionId));

        GuessResult result = action.apply(session);
        gameSessionRepositoryPort.save(session);

        if(result.roundStatus() != RoundStatus.PLAYING){
            updatePlayerAfterRound(session, result);
        }

        return result;
    }

    private void updatePlayerAfterRound(GameSession session, GuessResult result) {
        Player player = playerRepositoryPort.findById(session.getPlayerId())
                .orElseThrow(() -> new IllegalArgumentException("Player not found: " + session.getPlayerId()));

        player.recordScore(result.score());
        player.markSongAsPlayed(session.getSong().id());
        playerRepositoryPort.save(player);
    }
}