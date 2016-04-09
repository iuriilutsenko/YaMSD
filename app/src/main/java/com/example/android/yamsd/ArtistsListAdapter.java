package com.example.android.yamsd;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.yamsd.ArtistsData.Artist;

import java.util.ArrayList;

/**
 * Адаптер, нужен для того, чтобы потом беспроблемно прикручивать требуемые View-шки
 */
public class ArtistsListAdapter extends ArrayAdapter<Artist> {

    private Context context;

    private ArrayList<Artist> artists;

    public ArtistsListAdapter(Context context, int layoutId, int resourceId, ArrayList<Artist> artists) {
        super(context, layoutId, resourceId, artists);
        this.context = context;
        this.artists = artists;
    }

    public int getCount() {
        return artists.size();
    }

    public Artist getIten(int position) {
        return artists.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView singleArtistRecord = new TextView(context);
        singleArtistRecord
                .setText(
                        artists.get(position).name + " - " +
                                artists.get(position).tracksCount + " - " +
                                artists.get(position).albumsCount
                );
        return singleArtistRecord;
    }

    @Override
    public void clear() {
        super.clear();
    }
}
