package com.example.android.yamsd;

import android.content.Context;
import android.content.Intent;

/**
 * Класс, предназначенный для отправки Email-сообщений
 */
public class EmailSender {
    private static final String[] EMAIL = {"lutsenko.yuriy@yandex.ru"};


    public static void sendMessage(Context context) {
        Intent sendEmailIntent = new Intent(Intent.ACTION_SEND);
        sendEmailIntent.setType("message/rfc822");
        sendEmailIntent.putExtra(Intent.EXTRA_EMAIL, EMAIL);
        context.startActivity(sendEmailIntent);
    }
}
