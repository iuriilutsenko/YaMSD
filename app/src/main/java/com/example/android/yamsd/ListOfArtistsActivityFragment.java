package com.example.android.yamsd;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListOfArtistsActivityFragment extends Fragment {

    ArrayAdapter<String> artistsListAdapter;
    public ListOfArtistsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View listOfArtists = inflater.inflate(R.layout.fragment_list_of_artists, container, false);

        //Создание списка артистов
        //TODO - сделать так, чтобы отображалось, как в требованиях
        String[] artists = {
                "Artist 1",
                "Artist 2",
                "Artist 3"
        };
        List<String> artistsList = new ArrayList<String>(Arrays.asList(artists));

        artistsListAdapter =
                new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.single_artist,
                        R.id.single_artist,
                        artistsList
                );
        ListView listView = (ListView) listOfArtists.findViewById(R.id.artists_list);
        listView.setAdapter(artistsListAdapter);

        return listOfArtists;
    }
}
