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

    Artist (JSONObject jsonArtist) {
        try {
            this.id = jsonArtist.getInt("id");
            this.name = jsonArtist.getString("name");
            Log.v(LOG_TAG, this.name + this.id);

            JSONArray genresArray = jsonArtist.getJSONArray("genres");
            this.genres = new String[genresArray.length()];
            for (int i = 0; i < genresArray.length(); i++) {
                this.genres[i] = genresArray.getString(i);
                Log.v(LOG_TAG, this.genres[i]);
            }

            this.tracksCount = jsonArtist.getInt("tracks");
            this.albumsCount = jsonArtist.getInt("albums");

            this.link = jsonArtist.getString("link");
            this.description = jsonArtist.getString("description");

            this.smallCover =
                    Uri.parse(jsonArtist.getJSONObject("cover").getString("small"));
            this.bigCover =
                    Uri.parse(jsonArtist.getJSONObject("cover").getString("big"));
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Incorrect JSON: " + e);
        }
        catch (NullPointerException e) {
            Log.e(LOG_TAG, "NullPointerException: " + e);
        }
    }
}
