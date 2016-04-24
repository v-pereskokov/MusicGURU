package com.vlados.musicguru.Deserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.vlados.musicguru.Model.Artist;
import com.vlados.musicguru.Model.ArtistEntity;
import com.vlados.musicguru.Model.Cover;

import java.lang.reflect.Type;
/*
 * Десериалайзер для работы с json файлом
 */
public class ArtistDeserializer implements JsonDeserializer<ArtistEntity> {
    @Override
    public ArtistEntity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonArray()) {
            return null;
        }
        JsonArray jsonArray = json.getAsJsonArray();
        ArtistEntity artistList = new ArtistEntity();
        for (JsonElement jsonElement : jsonArray) {
            if (!jsonElement.isJsonObject())
                return null;
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Artist artist = new Artist();
            artist.setId(jsonObject.get("id").getAsInt());
            artist.setName(jsonObject.get("name").getAsString());
            artist.setTracks(jsonObject.get("tracks").getAsInt());
            artist.setAlbums(jsonObject.get("albums").getAsInt());
            String genres = "";
            for (int j = 0; j < jsonObject.getAsJsonArray("genres").size(); ++j) {
                if (j == jsonObject.getAsJsonArray("genres").size() - 1) {
                    genres += jsonObject.getAsJsonArray("genres").get(j).getAsString();
                } else {
                    genres += jsonObject.getAsJsonArray("genres").get(j).getAsString() + ", ";
                }
            }
            artist.setGenres(genres);
            JsonObject covers = jsonObject.get("cover").getAsJsonObject();
            Cover cover = new Cover();
            cover.setSmallCover(covers.get("small").getAsString());
            cover.setBigCover(covers.get("big").getAsString());
            artist.setCovers(cover);
            if (jsonObject.get("link") != null) {
                artist.setLink(jsonObject.get("link").getAsString());
            }
            artist.setDescription(jsonObject.get("description").getAsString());
            artistList.setArtistList(artist);
        }
        return artistList;
    }
}
