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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.yamsd.ArtistsData.Artist;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Адаптер, нужен для того, чтобы потом беспроблемно прикручивать требуемые View-шки
 */
public class ListOfArtistsAdapter extends ArrayAdapter<Artist> {

    //Кэш для хранения маленьких изображений
    private LruCache<String, Bitmap> imageCache = null;

    private String LOG_TAG = getClass().getSimpleName();

    private Context context;

    private ArrayList<Artist> artists;

    public ListOfArtistsAdapter(
            Context context,
            int layoutId,
            int resourceId,
            ArrayList<Artist> artists)
    {
        super(context, layoutId, resourceId, artists);
        this.context = context;
        this.artists = artists;

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory()) / 1024;
        final int cacheSize = maxMemory / 8;
        imageCache = new LruCache<String, Bitmap>(cacheSize) {
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

        //Данные об одном артисте
        View singleArtistRecord =
                LayoutInflater
                        .from(context)
                        .inflate(R.layout.single_artist_in_list, null);
        Artist singleArtistInfo = artists.get(position);

        //Изображение артиста
        ImageView singleArtistSmallImage =
                (ImageView) singleArtistRecord.findViewById(R.id.artist_image_small);
        loadImage(singleArtistInfo.id, singleArtistInfo, singleArtistSmallImage);

        //Название артиста
        TextView singleArtistTitle =
                (TextView) singleArtistRecord.findViewById(R.id.artist_title);
        singleArtistTitle.setText(singleArtistInfo.name);

        //Жанры артиста
        TextView singleArtistsGenres =
                (TextView) singleArtistRecord.findViewById(R.id.genres);
        String stringSingleArtistGenres = "";
        for (int i = 0; i < singleArtistInfo.genres.length; i++) {
            stringSingleArtistGenres += singleArtistInfo.genres[i];
            if (i < singleArtistInfo.genres.length - 1) {
                stringSingleArtistGenres += ", ";
            }
        }
        singleArtistsGenres.setText(stringSingleArtistGenres);

        //Данные о количестве альбомов и песен
        TextView singleArtistsAlbumsAndSongs =
                (TextView) singleArtistRecord.findViewById(R.id.albums_songs);
        singleArtistsAlbumsAndSongs
                .setText(singleArtistInfo.albumsCount + " " +
                        Utility.pluralize(singleArtistInfo.albumsCount, "альбом") + ", " +
                        singleArtistInfo.tracksCount + " " +
                        Utility.pluralize(singleArtistInfo.tracksCount, "песня")
                );
        return singleArtistRecord;
    }

    private void loadImage(int resId, Artist artist, ImageView view) {
        try {
            URL imageUrl = new URL(artist.smallCover);
            if (artist.smallCover == null) {
                return;
            }

            String imageId = String.valueOf(resId);
            Bitmap bitmap = getBitmapFromMemCache(imageId);

            if (bitmap != null) {
                view.setImageBitmap(bitmap);
            } else {
                new ImageLoadTask(view, imageId).execute(imageUrl);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Bad URL: " + e);
        }
    }

    private class ImageLoadTask extends AsyncTask<URL, Void, Bitmap> {
        ImageView smallCover;
        String imageIdInCache;

        public ImageLoadTask(ImageView view, String imageIdInCache) {
            this.smallCover = view;
            this.imageIdInCache = imageIdInCache;
        }

        @Override
        protected Bitmap doInBackground(URL... params) {
            try {
                return (Bitmap) Utility.downloadData(params[0], "bitmap");
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error while loading image: " + e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            smallCover.setImageBitmap(bitmap);
            addBitmapToMemoryCache(imageIdInCache, bitmap);
        }
    }

    //Функции для работы с кэшем изображений
    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            imageCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return imageCache.get(key);
    }

    @Override
    public void clear() {
        super.clear();
    }
}
