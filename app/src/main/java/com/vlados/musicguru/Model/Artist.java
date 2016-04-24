package com.vlados.musicguru.Model;
/*
 * Модель одного артиста
 * Все свойства класса - его характеристики в json файле
 */
public class Artist {
    private int id;
    private String name;
    private String genres;
    private int tracks;
    private int albums;
    private String link;
    private String description;
    private Cover covers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public int getTracks() {
        return this.tracks;
    }

    public void setTracks(int tracks) {
        this.tracks = tracks;
    }

    public int getAlbums() {
        return this.albums;
    }

    public void setAlbums(int albums) {
        this.albums = albums;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Cover getCovers() {
        return covers;
    }

    public void setCovers(Cover covers) {
        this.covers = covers;
    }
}
