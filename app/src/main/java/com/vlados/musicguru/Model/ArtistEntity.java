package com.vlados.musicguru.Model;

import java.util.ArrayList;
import java.util.List;
/*
 * Список всех артистов
 */
public class ArtistEntity {
    private List<Artist> artistList = new ArrayList<>();

    public List<Artist> getArtistList() {
        return artistList;
    }

    public void setArtistList(Artist artist) {
        this.artistList.add(artist);
    }
}
