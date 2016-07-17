package com.example.android.yamsd;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.yamsd.ArtistsData.Artist;

/**
 * Кэш для сохранения картинок.
 */
public class ImageStorage {

    private static ImageStorage instance = null;

    private String LOG_TAG = getClass().getSimpleName();

    private final Context context;
    private Network network = new BasicNetwork(new HurlStack());

    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static final int CACHE_SIZE = 100;

    public synchronized static ImageStorage getInstance(Context context) {
        if (instance == null) {
            instance = new ImageStorage(context);
        }

        return instance;
    }


    private ImageStorage(Context context) {

        this.context = context;

        requestQueue = getRequestQueue();

        imageLoader = new ImageLoader(
                requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap> cache =
                            new LruCache<>(CACHE_SIZE);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                }
        );
    }


    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }


    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }


    public ImageLoader getImageLoader() {
        return imageLoader;
    }


    public void getImage(
            Artist artist,
            final ArtistViewHolder viewHolder,

            String sizeOfImage
    ) {
        ImageRequest imageRequest =
                new ImageRequest(
                        sizeOfImage.equals("big") ?
                            artist.getBigCoverUrlString() :
                            artist.getSmallCoverUrlString(),

                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                viewHolder.cover.setImageBitmap(response);
                            }
                        },

                        0,
                        0,

                        ImageView.ScaleType.CENTER_CROP,
                        null,

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                viewHolder.cover.setImageResource(R.drawable.dummy_image);
                            }
                        }
                );

        this.addToRequestQueue(imageRequest);
    }

}
