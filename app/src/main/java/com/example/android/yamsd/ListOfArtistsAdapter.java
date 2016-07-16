package com.example.android.yamsd;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.android.yamsd.ArtistsData.Artist;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Адаптер списка артистов.
 */
public class ListOfArtistsAdapter extends ArrayAdapter<Artist> {

    private String LOG_TAG = getClass().getSimpleName();

    private Context context;

    private ArrayList<Artist> artists;
    private ImageCache imageCache = null;


    public ListOfArtistsAdapter(
            Context context,
            int layoutId,
            int resourceId,
            ArrayList<Artist> artists
    ) {
        super(context, layoutId, resourceId, artists);
        this.context = context;
        this.artists = artists;

        imageCache = new ImageCache();
    }


    public int getCount() {
        return artists.size();
    }


    public Artist getItem(int position) {
        return artists.get(position);
    }


    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        try {
            return loadListItem(artists.get(position)).getRootView();
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "getRootView() works incorrectly: " + e);
        }

        return null;
    }


    private ArtistViewHolder loadListItem(Artist artist) {
        ArtistViewHolder viewHolder =
                new ArtistViewHolder(
                    LayoutInflater.from(context),
                    "ListOfArtists",
                    artist
                );
        new loadSmallCoverTask(viewHolder).execute(artist);

        return viewHolder;
    }


    private class loadSmallCoverTask
            extends AsyncTask<Artist, Void, Bitmap> {
        private String LOG_TAG = getClass().getSimpleName();

        Artist artist;
        ArtistViewHolder viewHolder;

        public loadSmallCoverTask(ArtistViewHolder viewHolder) {
            this.viewHolder = viewHolder;
        }


        @Override
        protected Bitmap doInBackground(Artist... params) {

            try {
                artist = params[0];

                Bitmap bitmap =
                        imageCache.getBitmapFromMemCache(String.valueOf(artist.getId()));
                if (bitmap == null) {
                    bitmap = (Bitmap) Utility.downloadData(
                            new URL(params[0].getSmallCoverUrlString()),
                            "bitmap"
                    );
                    imageCache.addBitmapToMemCache(String.valueOf(artist.getId()), bitmap);
                }

                return bitmap;
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error while loading image: " + e);
            }

            return null;
        }


        @Override
        protected void onPostExecute(Bitmap bitmap) {
            viewHolder.setCoverBitmap(bitmap);
        }
    }


    @Override
    public void clear() {
        super.clear();
    }
}
