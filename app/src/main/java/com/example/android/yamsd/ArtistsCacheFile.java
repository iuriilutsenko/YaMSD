package com.example.android.yamsd;

import android.content.Context;
import android.util.Log;

import com.example.android.yamsd.ArtistsData.Artist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Кэш для хранения списка артистов в формате json
 */
public class ArtistsCacheFile {
    private String LOG_TAG = getClass().getSimpleName();

    private String cacheFileName = "artistsDownloaded";
    private Context context;

    File cacheFile;
    String artistsJsonFormat = null;


    public ArtistsCacheFile(Context context) {
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
        } catch (IOException e) {
            Log.e(LOG_TAG, "Some troubles at constructor: " + e);
        }
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
        InputStream inputStream = null;

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


    public Artist[] getArtistsFromCache() {
        if (artistsJsonFormat == null) {
            readFromCache();
        }
        return Utility.getArtists(artistsJsonFormat);
    }
}
