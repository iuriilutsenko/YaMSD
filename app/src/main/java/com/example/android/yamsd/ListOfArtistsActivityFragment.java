package com.example.android.yamsd;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.yamsd.ArtistsData.Artist;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListOfArtistsActivityFragment extends Fragment {
    private final String LOG_TAG = getClass().getSimpleName();

    private Artist[] artists = null;
    private ListOfArtistsAdapter listOfArtistsAdapter;

    private String site =
            "http://cache-default01e.cdn.yandex.net/" +
                    "download.cdn.yandex.net/mobilization-2016/artists.json";

    private String jsonArtists = null;
    private String fileCacheName = "artistsDownloaded";

    public ListOfArtistsActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            updateArtists(true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View listOfArtistsView =
                inflater.inflate(R.layout.fragment_list_of_artists, container, false);

        //Создание списка артистов
        File cache = new File(getActivity().getFilesDir(), fileCacheName);
        updateArtists(cacheInit(cache));

        ArrayList<Artist> artistsList = new ArrayList<>(Arrays.asList(artists));
        listOfArtistsAdapter =
                new ListOfArtistsAdapter(
                        getActivity(),
                        R.layout.single_artist_in_list,
                        R.id.single_artist_in_list,
                        artistsList
                );

        //Создание ListView, на элементы которой можно нажимать
        ListView listView = (ListView) listOfArtistsView.findViewById(R.id.artists_list);
        listView.setAdapter(listOfArtistsAdapter);
        listView.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Artist artist = artists[position];

                    Intent artistInfoIntent = new Intent(getActivity(), ArtistActivity.class);

                    artistInfoIntent.putExtra("id", artist.id);
                    artistInfoIntent.putExtra("name", artist.name);

                    artistInfoIntent.putExtra("genres", artist.genres);
                    artistInfoIntent.putExtra("tracks", artist.tracksCount);
                    artistInfoIntent.putExtra("albums", artist.albumsCount);

                    artistInfoIntent.putExtra("description", artist.description);

                    artistInfoIntent.putExtra("bigCover", artist.bigCover);
                    startActivity(artistInfoIntent);
                }
            }
        );

        return listOfArtistsView;
    }


    private void updateArtists(boolean refreshModeOn) {
        if (refreshModeOn) {
            // Костыль, срабатывающий при самом первом запуске приложения
            artists = getArtists("[{\n" +
                    "    \"id\": 0,\n" +
                    "    \"name\": \"0\",\n" +
                    "    \"genres\": [\n" +
                    "      \"rnb\"\n" +
                    "    ],\n" +
                    "    \"tracks\": 0,\n" +
                    "    \"albums\": 0,\n" +
                    "    \"link\": \"\",\n" +
                    "    \"description\": \"Nothing to say\",\n" +
                    "    \"cover\": {\n" +
                    "      \"small\": \"\",\n" +
                    "      \"big\": \"\"\n" +
                    "    }\n" +
                    "  }]");
            Toast.makeText(getContext(), "Обновление...", Toast.LENGTH_SHORT).show();
            new ArtistsLoaderTask().execute(site);
            Log.v(LOG_TAG, "Loaded from internet");
        } else {
            //Загрузка из файла реализована исключительно из соображений простоты реализации
            readFromCache();
            Log.v(LOG_TAG, "Loaded from cache");
        }
    }


    private class ArtistsLoaderTask extends AsyncTask<String, Void, Artist[]> {
        private final String LOG_TAG = getClass().getSimpleName();

        @Override
        protected Artist[] doInBackground(String... params) {
            try {
                return downloadArtists(params[0]);
            } catch (IOException e) {
                Log.e(LOG_TAG, "IOException: " + e);
                return null;
            }
        }

        private Artist[] downloadArtists(String siteWithArtists) throws IOException{
            InputStream jsonStream = null;
            URL urlWithArtists = new URL(siteWithArtists);
            HttpURLConnection downloadArtistsConnection =
                    (HttpURLConnection)urlWithArtists.openConnection();

            try {

                //Установка связи
                downloadArtistsConnection.setRequestMethod("GET");
                downloadArtistsConnection.setDoInput(true);

                //Начало скачивания
                downloadArtistsConnection.connect();
                int response = downloadArtistsConnection.getResponseCode();
                Log.v(LOG_TAG, "Response code: " + response);
                jsonStream = downloadArtistsConnection.getInputStream();

                //Перевод скаченной строки в артистов
                jsonArtists = readJsonString(jsonStream);
                writeToCache(jsonArtists);
                artists = getArtists(jsonArtists);
                return artists;
            } finally {
                if (downloadArtistsConnection != null) {
                    downloadArtistsConnection.disconnect();
                }
                if (jsonStream != null) {
                    try {
                        jsonStream.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "IOException: " + e);
                    }
                }
            }
        }

        //Считывание нужного нам файла
        private String readJsonString(InputStream jsonStream) throws IOException {
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

        @Override
        protected void onPostExecute(Artist[] result) {
            super.onPostExecute(result);

            listOfArtistsAdapter.clear();
            for(Artist artist:artists) {
                listOfArtistsAdapter.add(artist);
            }
            listOfArtistsAdapter.notifyDataSetChanged();
        }

    }

    private Artist[] getArtists(String jsonString) {
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

    //Проверка наличия и содержимого кэша
    private boolean cacheInit(File file) {
        try {
            if (file.exists()) {
                //Проверка пустоты кэша и его удаление, если кэш пуст
                BufferedReader buf =
                        new BufferedReader(new FileReader(file));

                if (buf.readLine() == null) {
                    file.delete();
                } else {
                    return false;
                }
            }

            return file.createNewFile();

        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException: " + e);
        }

        return false;
    }

    private void writeToCache(String string) {
        Log.v(LOG_TAG, "Writing to cache");
        FileOutputStream outputStream = null;

        try {
            outputStream = getContext().openFileOutput(fileCacheName, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException: " + e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "IOException: " + e);
            } catch (NullPointerException e) {
                Log.e(LOG_TAG, "Null Pointer: " + e);
            }
        }
    }

    private void readFromCache() {
        jsonArtists = "";
        InputStream inputStream = null;

        try {
            inputStream =
                    getContext().openFileInput(fileCacheName);

            if (inputStream != null) {
                InputStreamReader inputStreamReader =
                        new InputStreamReader(inputStream);
                BufferedReader bufferedReader =
                        new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                jsonArtists = stringBuilder.toString();
                artists = getArtists(jsonArtists);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException: " + e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "IOException: " + e);
            } catch (NullPointerException e) {
                Log.e(LOG_TAG, "Null Pointer: " + e);
            }
        }
    }
}
