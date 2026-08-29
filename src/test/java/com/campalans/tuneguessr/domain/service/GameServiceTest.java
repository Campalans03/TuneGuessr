package com.campalans.tuneguessr.domain.service;

import com.campalans.tuneguessr.domain.model.*;
import com.campalans.tuneguessr.domain.port.out.GameSessionRepositoryPort;
import com.campalans.tuneguessr.domain.port.out.PlayerRepositoryPort;
import com.campalans.tuneguessr.domain.port.out.SongCatalogPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private SongCatalogPort songCatalogPort;
    @Mock
    private PlayerRepositoryPort playerRepositoryPort;
    @Mock
    private GameSessionRepositoryPort gameSessionRepositoryPort;

    private GameService gameService;

    private final UUID playerId = UUID.randomUUID();
    private final UUID sessionId = UUID.randomUUID();
    private final Player player = Player.builder().id(playerId).name("Jan").build();
    private final Song song = new Song("1", "Song Title", "Artist", "url");

    @BeforeEach
    void setUp() {
        gameService = new GameService(
                songCatalogPort,
                playerRepositoryPort,
                gameSessionRepositoryPort,
                new GuessMatcher(),
                new ScoringPolicy()
        );
    }

    @Test
    void startGame_chooses_non_repeated_song_and_saves_the_session() {
        when(playerRepositoryPort.findById(playerId)).thenReturn(Optional.of(player));
        when(songCatalogPort.findRandomSongExcluding(player.getPlayedSongIds())).thenReturn(song);
        when(gameSessionRepositoryPort.save(any(GameSession.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        GameSession session = gameService.startGame(playerId);

        assertEquals(RoundStatus.PLAYING, session.getRoundStatus());
        assertEquals(1, session.getCurrentSnippetSeconds());
        assertEquals(song, session.getSong());
        verify(gameSessionRepositoryPort).save(session);
    }

    @Test
    void guessing_correctly_ends_the_round_and_updates_the_player() {
        GameSession session = GameSession.builder()
                .id(sessionId)
                .playerId(playerId)
                .song(song)
                .guessMatcher(new GuessMatcher())
                .scoringPolicy(new ScoringPolicy())
                .build();

        when(gameSessionRepositoryPort.findById(sessionId)).thenReturn(Optional.of(session));
        when(playerRepositoryPort.findById(playerId)).thenReturn(Optional.of(player));

        GuessResult result = gameService.submitGuess(sessionId, "Song Title");

        assertEquals(RoundStatus.WON, result.roundStatus());
        assertEquals(100, result.score());
        assertEquals(100, player.getTotalScore());
        assertTrue(player.getPlayedSongIds().contains(song.id()));
        verify(gameSessionRepositoryPort).save(session);
        verify(playerRepositoryPort).save(player);
    }

    @Test
    void giving_up_ends_the_round_and_updates_the_player() {
        GameSession session = GameSession.builder()
                .id(sessionId)
                .playerId(playerId)
                .song(song)
                .guessMatcher(new GuessMatcher())
                .scoringPolicy(new ScoringPolicy())
                .build();

        when(gameSessionRepositoryPort.findById(sessionId)).thenReturn(Optional.of(session));
        when(playerRepositoryPort.findById(playerId)).thenReturn(Optional.of(player));

        GuessResult result = gameService.giveUp(sessionId);

        assertEquals(RoundStatus.LOST, result.roundStatus());
        assertEquals(0, result.score());
        assertEquals(0, player.getTotalScore());
        assertTrue(player.getPlayedSongIds().contains(song.id()));
        verify(gameSessionRepositoryPort).save(session);
        verify(playerRepositoryPort).save(player);
    }

    @Test
    void skipping_round_5_times_ends_the_game() {
        GameSession session = GameSession.builder()
                .id(sessionId)
                .playerId(playerId)
                .song(song)
                .guessMatcher(new GuessMatcher())
                .scoringPolicy(new ScoringPolicy())
                .build();

        when(gameSessionRepositoryPort.findById(sessionId)).thenReturn(Optional.of(session));
        when(playerRepositoryPort.findById(playerId)).thenReturn(Optional.of(player));

        gameService.skipRound(sessionId);
        gameService.skipRound(sessionId);
        gameService.skipRound(sessionId);
        gameService.skipRound(sessionId);
        GuessResult result = gameService.skipRound(sessionId);

        assertEquals(RoundStatus.LOST, result.roundStatus());
        assertEquals(0, result.score());
        assertEquals(0, player.getTotalScore());
        assertTrue(player.getPlayedSongIds().contains(song.id()));
    }
}