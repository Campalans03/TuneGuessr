package com.campalans.tuneguessr.adapter.out.persistence;

import com.campalans.tuneguessr.adapter.out.persistence.entity.PlayerEntity;
import com.campalans.tuneguessr.domain.model.Player;
import com.campalans.tuneguessr.domain.port.out.PlayerRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaPlayerRepository implements PlayerRepositoryPort {

    private final PlayerJpaRepository playerJpaRepository;

    public JpaPlayerRepository(PlayerJpaRepository playerJpaRepository) {
        this.playerJpaRepository = playerJpaRepository;
    }

    @Override
    public Optional<Player> findById(UUID playerId) {
        Optional <PlayerEntity> playerEntity = playerJpaRepository.findById(playerId);
        return playerEntity.map(this::toDomainModel);
    }

    @Override
    public Player save(Player player) {
        PlayerEntity playerEntity = toEntityModel(player);
        playerEntity = playerJpaRepository.save(playerEntity);
        return toDomainModel(playerEntity);
    }

    private Player toDomainModel(PlayerEntity entity) {
        return Player.builder()
                .id(entity.getId())
                .name(entity.getName())
                .totalScore(entity.getTotalScore())
                .playedSongIds(entity.getPlayedSongIds())
                .build();
    }

    private PlayerEntity toEntityModel(Player player) {
        return new PlayerEntity(
                player.getId(),
                player.getName(),
                player.getTotalScore(),
                player.getPlayedSongIds()
        );
    }
}
