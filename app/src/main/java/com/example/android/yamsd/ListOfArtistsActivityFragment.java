package com.example.android.yamsd;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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

    private CacheAndListBuffer cacheAndListBuffer;


    public static ListOfArtistsActivityFragment newInstance() {
        return new ListOfArtistsActivityFragment();
    }


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
    public void onResume() {
        super.onResume();
        try {
            ((AppCompatActivity) getActivity())
                    .getSupportActionBar()
                    .setTitle("Исполнители");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            cacheAndListBuffer.updateArtists(true);
            return true;
        } else if (item.getItemId() == R.id.action_about) {
            AboutFragment aboutFragment =
                    AboutFragment.newInstance();

            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getFragmentManager().beginTransaction();

            fragmentTransaction.replace(
                    R.id.fragment_container,
                    aboutFragment
            );
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            return true;

        } else if (item.getItemId() == R.id.action_feedback) {

            EmailSender.sendMessage(getContext());

            return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View listOfArtistsView =
                inflater.inflate(R.layout.fragment_list_of_artists, container, false);

        cacheAndListBuffer =
                CacheAndListBuffer
                        .getCacheAndListBuffer(
                                getContext(),
                                getActivity()
                        );

        //Создание ListView, на элементы которой можно нажимать
        ListView listView = (ListView) listOfArtistsView.findViewById(R.id.artists_list);
        listView.setAdapter(cacheAndListBuffer.getListOfArtistsAdapter());
        listView.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    ArtistActivityFragment artistActivityFragment =
                            ArtistActivityFragment.newInstance(position);

                    android.support.v4.app.FragmentTransaction fragmentTransaction =
                            getFragmentManager().beginTransaction();

                    fragmentTransaction.replace(
                            R.id.fragment_container,
                            artistActivityFragment
                    );
                    fragmentTransaction.addToBackStack(null);

                    fragmentTransaction.commit();
                }
            }
        );

        return listOfArtistsView;
    }

}
