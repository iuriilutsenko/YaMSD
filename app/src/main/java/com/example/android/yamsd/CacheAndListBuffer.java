package com.example.android.yamsd;

import android.app.Activity;
import android.widget.ListView;

import com.example.android.yamsd.ArtistsData.Artist;

import java.util.ArrayList;

/**
 * Вспомогательный класс, обеспечивающий связь между
 * ArtistsListFragment и хранилищем артистов
 */
public class CacheAndListBuffer {

    private Activity activity;
    private ArtistsCache artistsCache;

    private static CacheAndListBuffer cacheAndListBuffer;

    public synchronized static CacheAndListBuffer getCacheAndListBuffer (
            Activity activity
    ) {
        if (cacheAndListBuffer == null) {
            cacheAndListBuffer = new CacheAndListBuffer(activity);
        }

        return cacheAndListBuffer;
    }

    private CacheAndListBuffer(Activity activity) {

        this.activity = activity;
        //Создание списка артистов
        artistsCache = new ArtistsCache(activity, this);
        artistsCache.updateArtists();
    }

    public void updateArtistsViewAdapter(
            ArrayList<Artist> artists
    ) {
        ListOfArtistsAdapter listOfArtistsAdapter = (ListOfArtistsAdapter)
                ((ListView) activity.findViewById(R.id.artists_list)).getAdapter();
        listOfArtistsAdapter.clear();
        for (Artist artist : artists) {
            listOfArtistsAdapter.add(artist);
        }

        listOfArtistsAdapter.notifyDataSetChanged();
    }

    public void updateArtists(boolean downloadArtists) {
        if (downloadArtists) {
            artistsCache.downloadArtistsFromCloud();
        } else {
            artistsCache.updateArtists();
        }
    }

    public ArrayList<Artist> getArtists() {
        return artistsCache.getArtists();
    }

}
