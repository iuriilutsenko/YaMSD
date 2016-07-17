package com.example.android.yamsd;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.android.yamsd.ArtistsData.Artist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * Кэш для хранения списка артистов
 * Загрузка из файла реализована исключительно из соображений простоты реализации
 */
public class ArtistsCache {

    private static ArtistsCache artistsCache = null;

    private String LOG_TAG = getClass().getSimpleName();

    private String siteWithArtists =
            "http://cache-default01e.cdn.yandex.net/" +
                    "download.cdn.yandex.net/mobilization-2016/artists.json";
    private String cacheFileName = "artistsDownloaded";
    private File cacheFile;
    private Context context;

    private String artistsJsonFormat = null;

    private ArrayList<Artist> artists = null;

    private CacheAndListBuffer cacheAndListBuffer;


    private ArtistsCache(
            Context context,
            CacheAndListBuffer cacheAndListBuffer
    ) {
        try {
            this.context = context;
            cacheFile = new File(context.getFilesDir(), cacheFileName);

            if (cacheFile.exists()) {
                //Проверка пустоты кэша и его удаление, если кэш пуст
                BufferedReader bufferedReader =
                        new BufferedReader(new FileReader(cacheFile));

                if (bufferedReader.readLine() == null) {
                    cacheFile.delete();
                } else {
                    readFromCache();
                }
            }

            cacheFile.createNewFile();

            this.cacheAndListBuffer = cacheAndListBuffer;

        } catch (IOException e) {
            Log.e(LOG_TAG, "Some troubles at constructor: " + e);
        }
    }


    public synchronized static ArtistsCache getInstance(
            Context context,
            CacheAndListBuffer cacheAndListBuffer
    ) {
        if (artistsCache == null) {
            artistsCache = new ArtistsCache(context, cacheAndListBuffer);
        }

        return artistsCache;
    }


    public void writeToCache(String string) {
        Log.v(LOG_TAG, "Writing to cache");
        FileOutputStream outputStream = null;

        try {

            outputStream = context
                    .openFileOutput(cacheFileName, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());

            artistsJsonFormat = string;

        } catch (IOException e) {
            Log.e(LOG_TAG, "Unable to write: " + e);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "Nothing to write to cache: " + e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "IOException: " + e);
            } catch (NullPointerException e) {
                Log.e(LOG_TAG, "Nothing to close: " + e);
            }
        }
    }


    public void readFromCache() {
        Log.v(LOG_TAG, "Reading from cache");
        InputStream inputStream;

        StringBuilder stringBuilder = new StringBuilder();

        try {
            inputStream =
                    context.openFileInput(cacheFileName);

            if (inputStream != null) {

                InputStreamReader inputStreamReader =
                        new InputStreamReader(inputStream);
                BufferedReader bufferedReader =
                        new BufferedReader(inputStreamReader);

                String receiveString;

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                artistsJsonFormat = stringBuilder.toString();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException: " + e);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "Null Pointer: " + e);
        }
    }


    public boolean notExistsOrEmpty() {
        try {
            if (cacheFile.exists()) {
                BufferedReader bufferedReader =
                        new BufferedReader(new FileReader(cacheFile));

                return bufferedReader.readLine() == null;
            }

            return !cacheFile.exists();
        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, "File not found while checking");
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while reading from file");
        }

        return true;
    }


    public void updateArtists() {
        if (notExistsOrEmpty()) {
            downloadArtistsFromCloud();
        } else {
            artists = downloadArtistsFromCache();
            Log.v(LOG_TAG, "Loaded from cache");
        }
    }


    public void downloadArtistsFromCloud() {
        //Костыль, срабатывающий при самом первом запуске приложения
        //Будет загрузка из данной ниже json-строки, а не из интернета
        //или кэша
        artists = Utility.getArtists("[{\n" +
                "    \"id\": -1,\n" +
                "    \"name\": \"Идет загрузка артистов\",\n" +
                "    \"genres\": [\n" +
                "      \"Пожалуйста, подождите\"\n" +
                "    ],\n" +
                "    \"tracks\": 0,\n" +
                "    \"albums\": 0,\n" +
                "    \"link\": \"\",\n" +
                "    \"description\": \"Nothing to say\",\n" +
                "    \"cover\": {\n" +
                "      \"small\": \"http://avatars.yandex.net/get-music-content/dfc531f5.p.1080505/300x300\",\n" +
                "      \"big\": \"http://avatars.yandex.net/get-music-content/dfc531f5.p.1080505/300x300\"\n" +
                "    }\n" +
                "  }]"
        );
        Toast.makeText(context, "Обновление...", Toast.LENGTH_SHORT).show();
        new ArtistsLoadingTask().execute(siteWithArtists);
        Log.v(LOG_TAG, "Loaded from internet");
    }


    public ArrayList<Artist> downloadArtistsFromCache() {
        if (artistsJsonFormat == null) {
            readFromCache();
        } else {
            artists = Utility.getArtists(artistsJsonFormat);
        }
        return artists;
    }


    public ArrayList<Artist> getArtists() {
        return artists;
    }


    private class ArtistsLoadingTask extends AsyncTask<String, Void, String> {
        private final String LOG_TAG = getClass().getSimpleName();

        @Override
        protected String doInBackground(String... params) {
            try {
                return (String) Utility.downloadData(new URL(params[0]));
            } catch (IOException e) {
                Log.e(LOG_TAG, "IOException: " + e);
                return null;
            }
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                artists = Utility.getArtists(result);
                writeToCache(result);
            }

            cacheAndListBuffer.updateArtistsViewAdapter(artists);
        }

    }
}
