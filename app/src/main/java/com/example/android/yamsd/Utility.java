package com.example.android.yamsd;

import android.graphics.Bitmap;
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

    public static String pluralize(int amount, String word) {
        int residue = amount % 100;

        if (11 <= residue && residue <= 19 ||
                residue % 10 == 0 ||
                5 <= residue % 10  && residue % 10 <= 9){
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



    public static Object downloadData(URL pageWithPicture, String dataType) throws IOException {
        Bitmap bitmap = null;
        HttpURLConnection downloadImageConnection =
                (HttpURLConnection)pageWithPicture.openConnection();
        InputStream imageStream = null;

        try {

            //Установка связи
            downloadImageConnection.setRequestMethod("GET");
            downloadImageConnection.setDoInput(true);

            //Скачивание
            downloadImageConnection.connect();
            int response = downloadImageConnection.getResponseCode();
            Log.v(LOG_TAG, "Response code: " + response);
            imageStream = downloadImageConnection.getInputStream();

            //Сохранение
            if (dataType.equals("bitmap")) {
                return BitmapFactory.decodeStream(imageStream);
            } else if (dataType.equals("json")) {
                return readJsonString(imageStream);
            }
        } finally {
            if (downloadImageConnection != null) {
                downloadImageConnection.disconnect();
            }
            if (imageStream != null) {
                try {
                    imageStream.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "IOException: " + e);
                }
            }
        }

        return null;
    }

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
        }

        return null;
    }
}