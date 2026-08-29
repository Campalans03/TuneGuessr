package com.campalans.tuneguessr.domain.port.out;

import com.campalans.tuneguessr.domain.model.Song;

import java.util.Set;

public interface SongCatalogPort {
    Song findRandomSongExcluding(Set<String> excludedSongIds);
}
