package com.example.android.yamsd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Фрагмент со списком артистов
 */
public class ListOfArtistsActivityFragment extends Fragment {
    private final String LOG_TAG = getClass().getSimpleName();

    private CacheAndListBuffer cacheAndListBuffer;

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
            cacheAndListBuffer.updateArtists(true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View listOfArtistsView =
                inflater.inflate(R.layout.fragment_list_of_artists, container, false);

        cacheAndListBuffer = new CacheAndListBuffer(getContext(), getActivity());

        //Создание ListView, на элементы которой можно нажимать
        ListView listView = (ListView) listOfArtistsView.findViewById(R.id.artists_list);
        listView.setAdapter(cacheAndListBuffer.getListOfArtistsAdapter());
        listView.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(createIntent(position));
                }
            }
        );

        return listOfArtistsView;
    }


    private Intent createIntent(int position) {
        Intent artistInfoIntent = new Intent(getActivity(), ArtistActivity.class);
        artistInfoIntent.putExtra("position", position);

        return artistInfoIntent;
    }

}
