package com.example.android.yamsd;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.yamsd.ArtistsData.Artist;

import java.io.IOException;
import java.net.URL;
/**
 * Фрагмент с информацией об одном артисте
 */

public class ArtistActivityFragment extends Fragment {

    private String LOG_TAG = getClass().getSimpleName();

    public ArtistActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {

            Intent artistInfoIntent = getActivity().getIntent();
            Artist artist =
                    new Artist(
                            artistInfoIntent.getStringExtra("name"),

                            artistInfoIntent.getStringArrayExtra("genres"),

                            artistInfoIntent.getIntExtra("albums", 0),
                            artistInfoIntent.getIntExtra("tracks", 0),

                            artistInfoIntent.getStringExtra("description"),

                            artistInfoIntent.getStringExtra("bigCover")
            );

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
        new loadArtistTask(viewHolder).execute(artist);

        return viewHolder;
    }

    private class loadArtistTask
            extends AsyncTask<Artist, Void, Bitmap> {
        Artist artist;

        ArtistViewHolder viewHolder;

        public loadArtistTask(ArtistViewHolder viewHolder) {
            this.viewHolder = viewHolder;
        }

        @Override
        protected Bitmap doInBackground(Artist... params) {
            try {
                artist = params[0];

                return (Bitmap) Utility.downloadData(
                        new URL(params[0].getBigCoverUrlString()),
                        "bitmap"
                );

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error while loading image: " + e);
            } catch (NullPointerException e){
                Log.e(LOG_TAG, "Null pointer while loading image: " + e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            viewHolder.setCoverBitmap(bitmap);
        }
    }
}
