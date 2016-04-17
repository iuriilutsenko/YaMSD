package com.example.android.yamsd;

import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.android.yamsd.ArtistsData.Artist;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Вспомогательный класс, созданный для вынесения повторяющихся функций
 */
public class Utility {

    private static String LOG_TAG = "Utility";

    //Вспомогательные функции для создания View-шек
    public static String pluralize(int amount, String word) {
        int residue = amount % 100;

        if (11 <= residue && residue <= 19 ||
                residue % 10 == 0 ||
                5 <= residue % 10  && residue % 10 <= 9) {
            if (word.equals("альбом")) {
                return "альбомов";
            } else if (word.equals("песня")) {
                return "песен";
            }
        }

        residue %= 10;
        if (residue == 1) {
            return word;
        } else {
            if (word.equals("альбом")) {
                return "альбома";
            } else if (word.equals("песня")) {
                return "песни";
            }
        }

        return null;
    }

    public static String getGenresAsSingleString(String[] genres) {
        String stringSingleArtistGenres = "";

        for (int i = 0; i < genres.length; i++) {
            stringSingleArtistGenres += genres[i];
            if (i < genres.length - 1) {
                stringSingleArtistGenres += ", ";
            }
        }

        return stringSingleArtistGenres;
    }

    public static String getAlbumsAndTracksAsSingleString(
            int albumsCount,
            int tracksCount
    ) {
        return new StringBuilder()

                .append(albumsCount)
                .append(" ")
                .append(pluralize(
                        albumsCount,
                        "альбом"
                ))

                .append(", ")

                .append(tracksCount)
                .append(" ")
                .append(Utility.pluralize(
                        tracksCount,
                        "песня"))

                .toString();
    }


    //Функция для загрузки данных (картинок или списка исполнителей) из интернета
    public static Object downloadData(URL pageWithPicture, String dataType) throws IOException {
        HttpURLConnection httpURLConnection =
                (HttpURLConnection)pageWithPicture.openConnection();
        InputStream inputStream = null;
        Object data = null;

        try {

            //Установка связи
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);

            //Скачивание
            httpURLConnection.connect();
            int response = httpURLConnection.getResponseCode();
            Log.v(LOG_TAG, "Response code: " + response);
            inputStream = httpURLConnection.getInputStream();

            if (dataType.equals("bitmap")) {
                data = BitmapFactory.decodeStream(inputStream);
            } else if (dataType.equals("json")) {
                data = readJsonString(inputStream);
            }
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "IOException: " + e);
                }
            }
        }

        return data;
    }


    //Функции для обработки скачанной json-строки
    public static String readJsonString(InputStream jsonStream) throws IOException {
        StringBuffer buffer = new StringBuffer();
        String jsonString;
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(jsonStream));

        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line).append("\n");
        }

        jsonString = buffer.toString();

        return jsonString;
    }

    public static Artist[] getArtists(String jsonString) {
        try {
            JSONArray jsonArtists =
                    new JSONArray(jsonString);
            Artist[] artistsList = new Artist[jsonArtists.length()];
            for (int i = 0; i < jsonArtists.length(); i++) {
                artistsList[i] = new Artist(jsonArtists.getJSONObject(i));
            }
            return artistsList;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Incorrect JSON: " + e);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "JSON not loaded properly: " + e);
        }

        return null;
    }
}
