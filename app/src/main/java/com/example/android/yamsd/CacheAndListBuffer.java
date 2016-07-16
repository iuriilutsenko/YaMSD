package com.example.android.yamsd;

import android.app.Activity;
import android.content.Context;

import com.example.android.yamsd.ArtistsData.Artist;

import java.util.ArrayList;

/**
 * Вспомогательный класс, обеспечивающий связь между
 * ListOfArtistsActivityFragment и хранилищем артистов
 */
public class CacheAndListBuffer {

    private ListOfArtistsAdapter listOfArtistsAdapter;
    private ArtistsCache artistsCache;


    public CacheAndListBuffer(Context context, Activity activity) {

        //Создание списка артистов
        artistsCache = new ArtistsCache(context, this);
        artistsCache.updateArtists();

        listOfArtistsAdapter =
                new ListOfArtistsAdapter(
                        activity,
                        R.layout.single_artist_in_list,
                        R.id.single_artist_in_list,
                        artistsCache.getArtists()
                );
    }


    public void updateArtistsViewAdapter(ArrayList<Artist> artists) {
        listOfArtistsAdapter.clear();
        for (Artist artist : artists) {
            listOfArtistsAdapter.add(artist);
        }

        listOfArtistsAdapter.notifyDataSetChanged();
    }


    public ListOfArtistsAdapter getListOfArtistsAdapter() {
        return listOfArtistsAdapter;
    }


    public ArtistsCache getArtistsCache() {
        return artistsCache;
    }


    public void updateArtists(boolean downloadArtists) {
        if (downloadArtists) {
            artistsCache.downloadArtistsFromCloud();
        } else {
            artistsCache.updateArtists();
        }
    }

}
