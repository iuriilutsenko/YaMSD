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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Фрагмент со списком артистов
 */

//TODO - реализовать поиск по артистам
public class ListOfArtistsActivityFragment extends Fragment {
    private final String LOG_TAG = getClass().getSimpleName();

    private Artist[] artists = null;
    private ListOfArtistsAdapter listOfArtistsAdapter;

    private String siteWithArtists =
            "http://cache-default01e.cdn.yandex.net/" +
                    "download.cdn.yandex.net/mobilization-2016/artists.json";

    private String artistsListJsonFormat = null;
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
                    startActivity(createIntent(artist));
                }
            }
        );

        return listOfArtistsView;
    }


    private Intent createIntent(Artist artist) {
        Intent artistInfoIntent = new Intent(getActivity(), ArtistActivity.class);

        artistInfoIntent.putExtra("id", artist.getId());
        artistInfoIntent.putExtra("name", artist.getName());

        artistInfoIntent.putExtra("genres", artist.getGenres());
        artistInfoIntent.putExtra("tracks", artist.getTracksCount());
        artistInfoIntent.putExtra("albums", artist.getAlbumsCount());

        artistInfoIntent.putExtra("description", artist.getDescription());

        artistInfoIntent.putExtra("bigCover", artist.getBigCoverUrlString());

        return artistInfoIntent;
    }


    private void updateArtists(boolean refreshModeOn) {
        if (refreshModeOn) {
            //Костыль, срабатывающий при самом первом запуске приложения
            //Будет загрузка из данной ниже json-строки, а не из интернета
            //или кэша
            artists = Utility.getArtists("[{\n" +
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
                    "      \"small\": \"http://avatars.yandex.net/get-music-content/dfc531f5.p.1080505/300x300\",\n" +
                    "      \"big\": \"http://avatars.yandex.net/get-music-content/dfc531f5.p.1080505/300x300\"\n" +
                    "    }\n" +
                    "  }]");
            Toast.makeText(getContext(), "Обновление...", Toast.LENGTH_SHORT).show();
            new ArtistsLoadingTask().execute(siteWithArtists);
            Log.v(LOG_TAG, "Loaded from internet");
        } else {
            //Загрузка из файла реализована исключительно из соображений простоты реализации
            readFromCache();
            Log.v(LOG_TAG, "Loaded from cache");
        }
    }


    private class ArtistsLoadingTask extends AsyncTask<String, Void, String> {
        private final String LOG_TAG = getClass().getSimpleName();

        @Override
        protected String doInBackground(String... params) {
            try {
                return (String) Utility.downloadData(new URL(params[0]), "json");
            } catch (IOException e) {
                Log.e(LOG_TAG, "IOException: " + e);
                return null;
            }
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            artists = Utility.getArtists(result);

            if (result != null) {

                listOfArtistsAdapter.clear();
                for (Artist artist : artists) {
                    listOfArtistsAdapter.add(artist);
                }

            }
            writeToCache(result);
            listOfArtistsAdapter.notifyDataSetChanged();
        }

    }


    //Функции для работы с кэшем
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


    private void readFromCache() {
        Log.v(LOG_TAG, "Reading from cache");
        artistsListJsonFormat = "";
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

                artistsListJsonFormat = stringBuilder.toString();
                artists = Utility.getArtists(artistsListJsonFormat);
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
