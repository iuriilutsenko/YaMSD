package com.example.android.yamsd;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.android.yamsd.ArtistsData.Artist;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Адаптер, нужен для того, чтобы потом беспроблемно прикручивать требуемые View-шки
 */
public class ListOfArtistsAdapter extends ArrayAdapter<Artist> {

    //Кэш для хранения маленьких изображений
    private LruCache<String, Bitmap> artistListItemCache = null;

    private String LOG_TAG = getClass().getSimpleName();

    private Context context;

    private ArrayList<Artist> artists;


    public ListOfArtistsAdapter(
            Context context,
            int layoutId,
            int resourceId,
            ArrayList<Artist> artists
    ) {
        super(context, layoutId, resourceId, artists);
        this.context = context;
        this.artists = artists;

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory()) / 1024;
        final int cacheSize = maxMemory / 8;
        artistListItemCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
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
        new listItemLoadTask(viewHolder).execute(artist);

        return viewHolder;
    }


    private class listItemLoadTask
            extends AsyncTask<Artist, Void, Bitmap> {
        Artist artist;

        ArtistViewHolder viewHolder;

        public listItemLoadTask(ArtistViewHolder viewHolder) {
            this.viewHolder = viewHolder;
        }

        @Override
        protected Bitmap doInBackground(Artist... params) {

            try {
                artist = params[0];

                Bitmap bitmap = getBitmapFromMemCache(String.valueOf(artist.getId()));
                if (bitmap == null) {
                    bitmap = (Bitmap) Utility.downloadData(
                            new URL(params[0].getSmallCoverUrlString()),
                            "bitmap"
                    );
                    addBitmapToMemoryCache(String.valueOf(artist.getId()), bitmap);
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


    //Функции для работы с кэшем изображений
    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        try {
            if (getBitmapFromMemCache(key) == null) {
                artistListItemCache.put(key, bitmap);
            }
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "Unable to load image to cache");
        }
    }


    private Bitmap getBitmapFromMemCache(String key) {
        return artistListItemCache.get(key);
    }


    @Override
    public void clear() {
        super.clear();
    }
}
