package com.example.android.yamsd;

import com.example.android.yamsd.ArtistsData.Artist;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ArtistTest {
    private static final String jsonString = "{\n" +
            "  \"id\": 1080505,\n" +
            "  \"name\": \"Tove Lo\",\n" +
            "  \"genres\": [\n" +
            "    \"pop\",\n" +
            "    \"dance\",\n" +
            "    \"electronics\"\n" +
            "  ],\n" +
            "  \"tracks\": 81,\n" +
            "  \"albums\": 22,\n" +
            "  \"link\": \"http://www.tove-lo.com/\",\n" +
            "  \"description\": \"laksdjgafgsfnjknalfjkbnjhdfbkv\",\n" +
            "  \"cover\": {\n" +
            "    \"small\": \"http://avatars.yandex.net/get-music-content/dfc531f5.p.1080505/300x300\",\n" +
            "    \"big\": \"http://avatars.yandex.net/get-music-content/dfc531f5.p.1080505/1000x1000\"\n" +
            "  }\n" +
            "}";

    @Test
    public void ArtistTestIdAndName() throws Exception {
        JSONObject jsonTest = new JSONObject(jsonString);
        Artist testArtist = new Artist(jsonTest);

        assertEquals(jsonTest.getInt("id"), testArtist.getId());
        assertEquals(testArtist.getName(), jsonTest.getString("name"));
    };

    @Test
    public void ArtistTestGenres() throws Exception {
        JSONObject jsonTest = new JSONObject(jsonString);
        Artist testArtist = new Artist(jsonTest);

        JSONArray genresJsonArray = jsonTest.getJSONArray("genres");
        for (int i = 0; i < genresJsonArray.length(); i++) {
            assertEquals(genresJsonArray.getString(i), testArtist.getGenres()[i]);
        }
    }

    @Test
    public void ArtistTestTracksAndAlbums() throws Exception {
        JSONObject jsonTest = new JSONObject(jsonString);
        Artist testArtist = new Artist(jsonTest);

        assertEquals(jsonTest.getInt("tracks"), testArtist.getTracksCount());
        assertEquals(jsonTest.getInt("albums"), testArtist.getAlbumsCount());
    }

    @Test
    public void ArtistTestInfo() throws Exception {
        JSONObject jsonTest = new JSONObject(jsonString);
        Artist testArtist = new Artist(jsonTest);

        assertEquals(jsonTest.getString("link"), testArtist.getLink());
        assertEquals(
                jsonTest.getString("description"),
                testArtist.getDescription()
        );
    }

    @Test
    public void ArtistTestCovers() throws Exception {
        JSONObject jsonTest = new JSONObject(jsonString);
        Artist testArtist = new Artist(jsonTest);

        assertEquals(
                    jsonTest.getJSONObject("cover").getString("small"),
                    testArtist.getSmallCoverUrlString()
        );
        assertEquals(
                    jsonTest.getJSONObject("cover").getString("big"),
                    testArtist.getBigCoverUrlString()
        );
    }

    @Test
    public void ArtistTestExceptionHandler() throws Exception {
        JSONObject jsonTest = new JSONObject();
        Artist testArtist = new Artist(jsonTest);

        jsonTest.put("id", 123456);
        testArtist = new Artist(jsonTest);

        jsonTest.put("name", "Splean");
        testArtist = new Artist(jsonTest);

        JSONArray jsonGenres = new JSONArray("[\"Rock\"]");
        jsonTest.put("genres", jsonGenres);
        testArtist = new Artist(jsonTest);

        jsonTest.put("tracks", 200);
        testArtist = new Artist(jsonTest);

        jsonTest.put("albums", 10);
        testArtist = new Artist(jsonTest);

        jsonTest.put("link", "http://splean.ru");
        testArtist = new Artist(jsonTest);

        jsonTest.put("description", "Очень классная группа");
        testArtist = new Artist(jsonTest);

        JSONObject covers = new JSONObject("{\n" +
                "    \"small\": \"http://avatars.yandex.net/get-music-content/dfc531f5.p.1080505/300x300\",\n" +
                "    \"big\": \"http://avatars.yandex.net/get-music-content/dfc531f5.p.1080505/1000x1000\"\n" +
                "  }"
        );
        jsonTest.put("cover", covers);
        testArtist = new Artist(jsonTest);
    }
}