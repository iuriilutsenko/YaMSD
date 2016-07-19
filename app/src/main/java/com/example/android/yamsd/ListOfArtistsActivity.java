package com.example.android.yamsd;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

public class ListOfArtistsActivity extends AppCompatActivity {

    private final String LOG_TAG = getClass().getSimpleName();

    private HeadPhonesPluggedReceiver headPhonesPluggedReceiver;
    IntentFilter headPhonesIntentFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_artists);

        headPhonesPluggedReceiver = new HeadPhonesPluggedReceiver();
        headPhonesIntentFilter =
                new IntentFilter(Intent.ACTION_HEADSET_PLUG);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Начинается реализация сменяемого объекта
        ListOfArtistsActivityFragment listOfArtistsActivityFragment =
                ListOfArtistsActivityFragment.newInstance();

        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(
                R.id.fragment_container,
                listOfArtistsActivityFragment
        );

        fragmentTransaction.commit();

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
}
