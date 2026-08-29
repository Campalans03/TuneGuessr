package com.campalans.tuneguessr.adapter.out.persistence;

import com.campalans.tuneguessr.domain.model.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(JpaPlayerRepository.class)
public class JpaPlayerRepositoryTest {

    @Autowired
    private JpaPlayerRepository repository;

    @Test
    void save_and_find_a_player_by_id() {
        UUID playerId = UUID.randomUUID();
        Player player = Player.builder()
                .id(playerId)
                .name("Jan")
                .totalScore(50)
                .playedSongIds(Set.of("1", "2"))
                .build();

        repository.save(player);

        Optional<Player> found = repository.findById(playerId);

        assertTrue(found.isPresent());
        assertEquals("Jan", found.get().getName());
        assertEquals(50, found.get().getTotalScore());
        assertTrue(found.get().getPlayedSongIds().contains("1"));
    }
}