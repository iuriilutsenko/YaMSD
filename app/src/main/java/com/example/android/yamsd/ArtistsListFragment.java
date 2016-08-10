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
public class ArtistsListFragment extends Fragment {

    private CacheAndListBuffer cacheAndListBuffer;

    public static ArtistsListFragment newInstance() {
        return new ArtistsListFragment();
    }

    public ArtistsListFragment() {
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
        switch (item.getItemId()) {
            case R.id.action_refresh:
                cacheAndListBuffer.updateArtists(true);
                return true;
            case R.id.action_about:
                AboutFragment aboutFragment =
                        AboutFragment.newInstance();

                getFragmentManager()
                        .beginTransaction()
                        .replace(
                                R.id.fragment_container,
                                aboutFragment
                        )
                        .addToBackStack(null)
                        .commit();

                return true;

            case R.id.action_feedback:
                EmailSender.sendMessage(getContext());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View listOfArtistsView =
                inflater.inflate(R.layout.fragment_list_of_artists, container, false);

        cacheAndListBuffer =
                CacheAndListBuffer
                        .getCacheAndListBuffer(
                                getActivity()
                        );

        ListView listView = (ListView) listOfArtistsView.findViewById(R.id.artists_list);
        listView.setAdapter(new ListOfArtistsAdapter(
                getContext(),
                R.layout.fragment_list_of_artists,
                R.id.artists_list,
                cacheAndListBuffer.getArtists()
        ));
        listView.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    ArtistActivityFragment artistActivityFragment =
                            ArtistActivityFragment.newInstance(position);

                    getFragmentManager()
                            .beginTransaction()
                            .replace(
                                    R.id.fragment_container,
                                    artistActivityFragment
                            )
                            .addToBackStack(null)
                            .commit();
                }
            }
        );

        return listOfArtistsView;
    }

}
