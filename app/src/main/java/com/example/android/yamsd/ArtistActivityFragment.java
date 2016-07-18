package com.example.android.yamsd;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.yamsd.ArtistsData.Artist;
import com.squareup.picasso.Picasso;

/**
 * Фрагмент с информацией об одном артисте.
 */

public class ArtistActivityFragment extends Fragment {

    private String LOG_TAG = getClass().getSimpleName();

    Artist artist;


    public static ArtistActivityFragment newInstance(int index) {
        ArtistActivityFragment artistActivityFragment =
                new ArtistActivityFragment();

        Bundle args = new Bundle();
        args.putInt("index", index);
        artistActivityFragment.setArguments(args);

        return artistActivityFragment;
    }


    public ArtistActivityFragment() {
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
            CacheAndListBuffer
                    .getCacheAndListBuffer(
                            getContext(),
                            getActivity()
                    )
                    .updateArtists(true);
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

        try {

            int artistInfoIndex = getArguments().getInt("index", 0);
            artist =
                    CacheAndListBuffer
                            .getCacheAndListBuffer(
                                    getContext(),
                                    getActivity()
                            )
                            .getArtists()
                            .get(artistInfoIndex);

            return loadArtistData(artist, inflater).getRootView();
        } catch (NullPointerException e) {
            Log.e(
                    LOG_TAG,
                    "Null Pointer Exception while creating view: " + e
            );
        }

        return null;
    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            ((AppCompatActivity) getActivity())
                    .getSupportActionBar()
                    .setTitle(artist.getName());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    private ArtistViewHolder loadArtistData(
            Artist artist,
            LayoutInflater inflater
    ) {
        ArtistViewHolder viewHolder =
                new ArtistViewHolder(
                        inflater,
                        "Artist",
                        artist
                );

        Picasso
                .with(getContext())
                .load(artist.getBigCoverUrlString())
                .into(viewHolder.cover);

        return viewHolder;
    }


}
