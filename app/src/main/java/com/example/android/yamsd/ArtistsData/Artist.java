package com.example.android.yamsd.ArtistsData;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Класс артиста
 *
 */
public class Artist {
    private String LOG_TAG = getClass().getSimpleName();

    private int id;
    private String name;

    private String[] genres;
    private int tracksCount;
    private int albumsCount;

    private String link = null;
    private String description;

    private String smallCoverUrlString;
    private String bigCoverUrlString;

    private final String _id = "id";
    private final String _name = "name";

    private final String _genres = "genres";
    private final String _tracks = "tracks";
    private final String _albums = "albums";

    private final String _link = "link";
    private final String _description = "description";

    private final String _cover = "cover";
    private final String _bigCover = "big";
    private final String _smallCover = "small";

    public Artist(JSONObject jsonArtist) {
        try {
            this.id = jsonArtist.getInt(_id);
            this.name = jsonArtist.getString(_name);

            JSONArray genresArray = jsonArtist.getJSONArray(_genres);
            this.genres = new String[genresArray.length()];
            for (int i = 0; i < genresArray.length(); i++) {
                this.genres[i] = genresArray.getString(i);
            }

            this.tracksCount = jsonArtist.getInt(_tracks);
            this.albumsCount = jsonArtist.getInt(_albums);

            if (jsonArtist.has(_link)) {
                this.link = jsonArtist.getString(_link);
            }
            this.description = jsonArtist.getString(_description);

            this.smallCoverUrlString =
                    jsonArtist.getJSONObject(_cover).getString(_smallCover);
            this.bigCoverUrlString =
                    jsonArtist.getJSONObject(_cover).getString(_bigCover);

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Incorrect JSON: " + e);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "Something Not Passed: " + e);
        }
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public String[] getGenres() {
        return genres;
    }

    public int getAlbumsCount() {
        return albumsCount;
    }

    public int getTracksCount() {
        return tracksCount;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getSmallCoverUrlString() {
        return smallCoverUrlString;
    }

    public String getBigCoverUrlString() {
        return bigCoverUrlString;
    }
}
