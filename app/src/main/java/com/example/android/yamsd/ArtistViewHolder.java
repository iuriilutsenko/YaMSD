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


    public ArtistViewHolder (
            LayoutInflater inflater,
            String activity,

            Artist artist
    ) {
        this.inflater = inflater;

        this.artist = artist;

        if (activity.equals("ListOfArtists")) {
            inflateViewsForList(R.layout.single_artist_in_list);
        } else if (activity.equals("Artist")) {
            inflateViewsForList(R.layout.fragment_artist);
        }
    }


    private void inflateViewsForList(int layoutId) {
        rootView =
                inflater.inflate(layoutId, null);

        //Изображение артиста
        cover = (ImageView) rootView.findViewById(R.id.artist_image);

        //Жанры артиста
        genres = (TextView) rootView.findViewById(R.id.artist_genres);
        genres.setText(
                Utility.getGenresAsSingleString(
                        artist.getGenres()
                )
        );

        //Данные о количестве альбомов и песен
        albumsAndTracks =
                (TextView) rootView.findViewById(R.id.albums_tracks);
        albumsAndTracks
                .setText(
                        Utility.getAlbumsAndTracksAsString(
                                artist.getAlbumsCount(),
                                artist.getTracksCount()
                        )
                );

        if (layoutId == R.layout.fragment_artist) {
            description =
                    (TextView) rootView.findViewById(R.id.description);
            description.setText(
                    String.format(
                            "%s - %s",
                            artist.getName(),
                            artist.getDescription()
                    )
            );
        } else if (layoutId == R.layout.single_artist_in_list) {
            //Название артиста
            title = (TextView) rootView.findViewById(R.id.artist_title);
            title.setText(artist.getName());
        }
    }


    public View getRootView() {
        return rootView;
    }


    public void setCoverBitmap(Bitmap coverBitmap) {
        cover.setImageBitmap(coverBitmap);
    }
}
