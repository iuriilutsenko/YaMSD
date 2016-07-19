package com.example.android.yamsd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * BroadcastReceiver вставленных наушников
 */
public class HeadPhonesPluggedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);

            //TODO - реализовать работу с наушниками
            switch(state) {
                case 0:
                    Toast.makeText(
                            context,
                            "Наушники вынуты",
                            Toast.LENGTH_SHORT
                    ).show();
                    break;
                case 1:
                    Toast.makeText(
                            context,
                            "Наушники вставлены",
                            Toast.LENGTH_SHORT
                    ).show();
                    break;
                default:
                    Log.e("HeadPhonesPluggedRcvr", "Что не так с наушниками?");
                    break;
            }
        }
    }
}
