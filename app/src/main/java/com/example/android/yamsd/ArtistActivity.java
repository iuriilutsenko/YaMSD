package com.example.android.yamsd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

/*
 *  Описание Activity, отвечающей за вывод информации об отдельном исполнителе
 */
public class ArtistActivity extends AppCompatActivity {

    private final String LOG_TAG = getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_artist);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getIntent().getStringExtra("name"));
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "Null pointer: " + e);
        }
    }

}
