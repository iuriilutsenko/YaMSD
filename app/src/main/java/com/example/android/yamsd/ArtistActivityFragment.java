package com.example.android.yamsd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Фрагмент с информацией об одном артисте
 */
public class ArtistActivityFragment extends Fragment {

    public ArtistActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View artistInfo = inflater.inflate(R.layout.fragment_artist, container, false);

        Intent artistInfoIntent = getActivity().getIntent();
        String artistName = artistInfoIntent.getStringExtra("name");
        String artistDescription = artistInfoIntent.getStringExtra("description");

        TextView info = (TextView) artistInfo.findViewById(R.id.artist_info);
        info.setText(artistName + " - " + artistDescription);

        return artistInfo;
    }
}
