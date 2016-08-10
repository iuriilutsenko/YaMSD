package com.example.android.yamsd;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

public class ListOfArtistsActivity extends AppCompatActivity {

    private final String LOG_TAG = getClass().getSimpleName();

    HeadPhonesPluggedReceiver headPhonesPluggedReceiver;
    IntentFilter headPhonesIntentFilter;

    NotificationReaction notificationReaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_artists);

        notificationReaction = new NotificationReaction(this);

        headPhonesPluggedReceiver =
                HeadPhonesPluggedReceiver.newReceiver(notificationReaction);
        headPhonesIntentFilter =
                new IntentFilter(Intent.ACTION_HEADSET_PLUG);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Начинается реализация сменяемого объекта
        ArtistsListFragment artistsListFragment =
                ArtistsListFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                    R.id.fragment_container,
                    artistsListFragment
                )
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_of_artists, menu);
        return true;
    }


    @Override
    protected void onResume() {
        registerReceiver(headPhonesPluggedReceiver, headPhonesIntentFilter);

        super.onResume();
    }

    @Override
    protected void onPause() {
        notificationReaction.stopNotification();
        unregisterReceiver(headPhonesPluggedReceiver);

        super.onPause();
    }
}
