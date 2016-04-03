package com.example.android.yamsd;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListOfArtistsActivityFragment extends Fragment {
    private final String LOG_TAG = getClass().getSimpleName();

    private Artist[] artists;
    private ArrayAdapter<String> artistsListAdapter;

    public ListOfArtistsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            View listOfArtistsView = inflater.inflate(R.layout.fragment_list_of_artists, container, false);

            //Создание списка артистов
            //TODO - сделать так, чтобы отображалось, как в требованиях
            JSONArray jsonArtists = new ArtistsLoaderTask().getArtists();
            artists = new Artist[jsonArtists.length()];
            String[] artistsStrings = new String[jsonArtists.length()];
            for (int i = 0; i < jsonArtists.length(); i++) {
                artists[i] = new Artist(jsonArtists.getJSONObject(i));
                artistsStrings[i] = artists[i].name + " - " +
                        artists[i].albumsCount + " - " +
                        artists[i].tracksCount;
            }
            List<String> artistsList = new ArrayList<String>(Arrays.asList(artistsStrings));

            artistsListAdapter =
                    new ArrayAdapter<String>(
                            getActivity(),
                            R.layout.single_artist,
                            R.id.single_artist,
                            artistsList
                    );
            ListView listView = (ListView) listOfArtistsView.findViewById(R.id.artists_list);
            listView.setAdapter(artistsListAdapter);

            return listOfArtistsView;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Incorrect JSON: " + e);
        }

        return null;
    }
}
