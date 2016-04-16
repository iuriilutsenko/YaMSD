package com.example.android.yamsd;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.yamsd.ArtistsData.Artist;

/**
 * Класс ArtistViewHolder
 */
public class ArtistViewHolder {
    LayoutInflater inflater;
    View rootView;

    Artist artist;

    ImageView cover;

    TextView title;

    TextView genres;
    TextView albumsAndTracks;

    TextView description;


    public ArtistViewHolder(
            LayoutInflater inflater,
            String activity,

            Artist artist
    ) {
        this.inflater = inflater;

        this.artist = artist;

        if (activity.equals("ListOfArtists")) {
            inflateAsListOfArtists();
        } else if (activity.equals("SingleArtist")) {
            //inflateAsSingleArtist();
        }
    }

    private void inflateAsListOfArtists() {
        rootView =
                inflater.inflate(R.layout.single_artist_in_list, null);

        //Изображение артиста
        cover = (ImageView) rootView.findViewById(R.id.artist_image_small);

        //Название артиста
        title = (TextView) rootView.findViewById(R.id.artist_title);
        title.setText(artist.getName());

        //Жанры артиста
        genres = (TextView) rootView.findViewById(R.id.genres);
        genres.setText(
                Utility.getGenresAsSingleString(
                        artist.getGenres()
                )
        );

        //Данные о количестве альбомов и песен
        albumsAndTracks =
                (TextView) rootView.findViewById(R.id.albums_songs);
        albumsAndTracks
                .setText(
                        Utility.getAlbumsAndTracksAsSingleString(
                                artist.getAlbumsCount(),
                                artist.getTracksCount()
                        )
                );

        description = null;
    }

    public View getRootView() {
        return rootView;
    }

    public void setCoverBitmap(Bitmap coverBitmap) {
        cover.setImageBitmap(coverBitmap);
    }
}
