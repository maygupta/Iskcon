package com.iskcon.pb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by maygupta on 12/20/15.
 */
public class CustomReceiver extends ParsePushBroadcastReceiver{
    private final String PARSE_NOTIFICATIONS = "parse_notifications";

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        boolean showNotifications = sharedPreferences.getBoolean(PARSE_NOTIFICATIONS, true);
        if (showNotifications) {
            super.onPushReceive(context, intent);
        }
    }
}
