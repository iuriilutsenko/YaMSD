package com.example.android.yamsd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * BroadcastReceiver вставленных наушников
 */
public class HeadPhonesPluggedReceiver extends BroadcastReceiver {

    NotificationReaction notificationReaction;

    public static HeadPhonesPluggedReceiver newReceiver(
            NotificationReaction notificationReaction
    ) {
        return new HeadPhonesPluggedReceiver(notificationReaction);
    }


    private HeadPhonesPluggedReceiver(NotificationReaction notificationReaction) {
        this.notificationReaction = notificationReaction;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);

            switch(state) {
                case 0:
                    notificationReaction.stopNotification();
                    break;
                case 1:
                    notificationReaction.startNotification();
                    break;
                default:
                    Log.e("HeadPhonesPluggedRcvr", "Что не так с наушниками?");
                    break;
            }
        }
    }
}
