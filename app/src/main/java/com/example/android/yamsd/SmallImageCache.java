package com.example.android.yamsd;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 * Кэш для сохранения маленьких картинок.
 */
public class SmallImageCache {

    private String LOG_TAG = getClass().getSimpleName();

    private LruCache<String, Bitmap> stringBitmapLruCache;

    final int maxMemory = (int) (Runtime.getRuntime().maxMemory()) / 1024;
    final int cacheSize = maxMemory / 8;


    public SmallImageCache() {

        stringBitmapLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }


    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        try {
            if (getBitmapFromMemCache(key) == null) {
                stringBitmapLruCache.put(key, bitmap);
            }
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "Unable to load image to cache");
        }
    }


    public Bitmap getBitmapFromMemCache(String key) {
        return stringBitmapLruCache.get(key);
    }
}
