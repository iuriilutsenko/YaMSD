package com.example.android.yamsd;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yurich on 02.04.16.
 *
 */
public class Artist {
    private String LOG_TAG = getClass().getSimpleName();

    public int id;
    public String name;

    public String[] genres;
    public int tracksCount;
    public int albumsCount;

    public String link;
    public String description;

    public Uri smallCover;
    public Uri bigCover;

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

    Artist (JSONObject jsonArtist) {
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

            this.link = jsonArtist.getString(_link);
            this.description = jsonArtist.getString(_description);

            this.smallCover =
                    Uri.parse(jsonArtist.getJSONObject(_cover).getString(_smallCover));
            this.bigCover =
                    Uri.parse(jsonArtist.getJSONObject(_cover).getString(_bigCover));

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Incorrect JSON: " + e);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "Something Not Passed: " + e);
        }
    }
}
