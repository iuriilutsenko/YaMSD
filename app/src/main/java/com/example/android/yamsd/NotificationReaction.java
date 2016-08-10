package com.example.android.yamsd;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

/**
 * Класс, описывающий взаимодействие
 * пользователя с нотификацией при воткнутых
 * наушниках
 */
public class NotificationReaction {

    public static String MUSIC_APP = "yandexmusic://";
    public static String RADIO_APP = "yandexradio://";

    public static String MUSIC_ID = "ru.yandex.music";
    public static String RADIO_ID = "ru.yandex.radio";

    public static String MARKET = "market://details?id=";

    private Context context;

    private NotificationCompat.Builder builder;
    private NotificationManager manager;

    RemoteViews remoteViews;


    public NotificationReaction(Context context) {
        this.context = context;
        remoteViews =
                new RemoteViews(
                        context.getPackageName(),
                        R.layout.notification_headset_plugged
                );

        builder = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.icon_add)
                .setContent(remoteViews);

        createPendingIntent(MUSIC_APP, MUSIC_ID, R.id.LISTEN_TO_MUSIC_BUTTON);
        createPendingIntent(RADIO_APP, RADIO_ID, R.id.LISTEN_TO_RADIO_BUTTON);

        manager =
                (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
    }


    private void createPendingIntent(
            String kindOfIntentForApp,
            String packageName,
            int buttonId
    ) {
        Intent intent = null;
        PendingIntent musicPendingIntent = null;

        if (Utility.appInstalled(context, packageName)) {
            intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(kindOfIntentForApp)
            );
        } else {
            intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(MARKET + packageName)
            );
        }

        musicPendingIntent =
                PendingIntent.getActivity(context, 0, intent, 0);

        remoteViews.setOnClickPendingIntent(
                buttonId,
                musicPendingIntent
        );
    }

    public void startNotification() {
        manager.notify(0, builder.build());
    }

    public void stopNotification() {
        manager.cancelAll();
    }
}
