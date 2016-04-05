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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.yamsd.ArtistsData.Artist;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListOfArtistsActivityFragment extends Fragment {
    private final String LOG_TAG = getClass().getSimpleName();

    private Artist[] artists = null;
    private ArrayAdapter<String> artistsListAdapter;

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
            Toast.makeText(getContext(), "Обновление...", Toast.LENGTH_SHORT).show();
            updateArtists(true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View listOfArtistsView = inflater.inflate(R.layout.fragment_list_of_artists, container, false);

        //Создание списка артистов
        //TODO - сделать так, чтобы отображалось, как в требованиях
        File cache = new File(fileCacheName);
        if (!cache.exists()) {
            try {
                cache.createNewFile();
                Log.v(LOG_TAG, "Made cache");
            } catch (IOException e) {
                Log.e(LOG_TAG, "IOException: " + e);
            }
        }
        updateArtists(!cache.exists());

        String[] artistsStrings = new String[artists.length];
        for (int i = 0; i < artists.length; i++) {
            artistsStrings[i] = artists[i].name + " - " +
                    artists[i].albumsCount + " - " +
                    artists[i].tracksCount;
        }
        List<String> artistsList = new ArrayList<String>(Arrays.asList(artistsStrings));

        artistsListAdapter =
                new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.single_artist_in_list,
                        R.id.single_artist_in_list,
                        artistsList
                );

        //Создание ListView, на элементы которой можно нажимать
        ListView listView = (ListView) listOfArtistsView.findViewById(R.id.artists_list);
        listView.setAdapter(artistsListAdapter);
        listView.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent artistInfoIntent = new Intent(getActivity(), ArtistActivity.class);
                    artistInfoIntent.putExtra("name", artists[position].name);
                    artistInfoIntent.putExtra("description", artists[position].description);
                    startActivity(artistInfoIntent);
                }
            }
        );

        return listOfArtistsView;


    }

    //
    private void updateArtists(boolean refreshModeOn) {
        if (refreshModeOn) {
            //TODO - исправить "костыль", чтобы программа не вылетала при запуске
            artists = getArtists("[{\n" +
                    "    \"id\": 1,\n" +
                    "    \"name\": \"Me\",\n" +
                    "    \"genres\": [\n" +
                    "      \"rnb\",\n" +
                    "      \"pop\",\n" +
                    "      \"rap\"\n" +
                    "    ],\n" +
                    "    \"tracks\": 256,\n" +
                    "    \"albums\": 152,\n" +
                    "    \"link\": \"http://www.neyothegentleman.com/\",\n" +
                    "    \"description\": \"аОаБаЛаАаДаАб\u0082аЕаЛб\u008C б\u0082б\u0080б\u0091б\u0085 аПб\u0080аЕаМаИаИ а\u0093б\u0080б\u008DаМаМаИ, аАаМаЕб\u0080аИаКаАаНб\u0081аКаИаЙ аПаЕаВаЕб\u0086, аАаВб\u0082аОб\u0080 аПаЕб\u0081аЕаН, аПб\u0080аОаДб\u008Eб\u0081аЕб\u0080, аАаКб\u0082б\u0091б\u0080, б\u0084аИаЛаАаНб\u0082б\u0080аОаП. а\u0092 2009 аГаОаДб\u0083 аЖб\u0083б\u0080аНаАаЛ Billboard аПаОб\u0081б\u0082аАаВаИаЛ а\u009DаИ-а\u0099аО аНаА 57 аМаЕб\u0081б\u0082аО аВ б\u0080аЕаЙб\u0082аИаНаГаЕ ТЋа\u0090б\u0080б\u0082аИб\u0081б\u0082б\u008B аДаЕб\u0081б\u008Fб\u0082аИаЛаЕб\u0082аИб\u008FТЛ.\",\n" +
                    "    \"cover\": {\n" +
                    "      \"small\": \"http://avatars.yandex.net/get-music-content/15ae00fc.p.2915/300x300\",\n" +
                    "      \"big\": \"http://avatars.yandex.net/get-music-content/15ae00fc.p.2915/1000x1000\"\n" +
                    "    }\n" +
                    "  }]");
            new ArtistsLoaderTask().execute(site);
            Log.v(LOG_TAG, "Loaded from internet");
        } else {
            Log.v(LOG_TAG, "Reading from Cache");
            //TODO - реализовать выгрузку с устройства
            //Загрузка из файла реализована исключительно из соображений простоты реализации
            readFromCache();
            Log.v(LOG_TAG, "Loaded from cache");
        }
    }



    //TODO - для реализованного ниже кода СРОЧНО ТРЕБУЕТСЯ РЕФАКТОРИНГ!!!!!
    private class ArtistsLoaderTask extends AsyncTask<String, Void, Artist[]> {
        private final String LOG_TAG = getClass().getSimpleName();

        @Override
        protected Artist[] doInBackground(String... params) {
            try {
                return downloadArtists(params[0]);
            } catch (IOException e) {
                Log.e(LOG_TAG, "IOException");
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
                writeDownToCache(jsonArtists);
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
                return null;
            }
        }

        //Считывание нужного нам файла
        private String readJsonString(InputStream jsonStream) throws IOException{;
            StringBuffer buffer = new StringBuffer();
            String jsonString;
            if (jsonStream == null) {
                jsonString = null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(jsonStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                jsonString = null;
            }
            jsonString = buffer.toString();

            return jsonString;
        }

        @Override
        protected void onPostExecute(Artist[] result) {
            super.onPostExecute(result);

            artistsListAdapter.clear();
            for(int i = 0; i < artists.length; i++) {
                artistsListAdapter.add(
                        artists[i].name + " - "
                                + artists[i].albumsCount + " - " +
                                artists[i].tracksCount
                );
            }
            artistsListAdapter.notifyDataSetChanged();

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

    private void writeDownToCache(String string) {
        FileOutputStream outputStream = null;

        try {
            outputStream = getActivity().openFileOutput(fileCacheName, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException: " + e);
        }
    }

    private void readFromCache() {
        jsonArtists = "";
        InputStream inputStream = null;

        try {
            inputStream =
                    getActivity().openFileInput(fileCacheName);

            if (inputStream != null) {
                InputStreamReader inputStreamReader =
                        new InputStreamReader(inputStream);
                BufferedReader bufferedReader =
                        new BufferedReader(inputStreamReader);
                String receiveString = "";
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
            }
        }
    }
}
