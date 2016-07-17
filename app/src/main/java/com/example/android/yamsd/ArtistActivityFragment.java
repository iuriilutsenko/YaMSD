package com.example.android.yamsd;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.yamsd.ArtistsData.Artist;
import com.squareup.picasso.Picasso;

/**
 * Фрагмент с информацией об одном артисте.
 */

public class ArtistActivityFragment extends Fragment {

    private String LOG_TAG = getClass().getSimpleName();


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {

            int artistInfoIndex = getArguments().getInt("index", 0);
            Artist artist =
                    ArtistsCache
                            .getInstance(getContext(), null)
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
