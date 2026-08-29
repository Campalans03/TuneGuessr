package com.campalans.tuneguessr.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "players")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerEntity {

    @Id
    private UUID id;
    private String name;
    private int totalScore;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "player_played_songs", joinColumns = @JoinColumn(name = "player_id"))
    @Column(name = "song_id")
    private Set<String> playedSongIds = new HashSet<>();
}
