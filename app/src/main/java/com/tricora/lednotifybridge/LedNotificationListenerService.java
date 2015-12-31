package com.tricora.lednotifybridge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Tille on 29.12.2015.
 */
public class LedNotificationListenerService extends NotificationListenerService {

    private Communicator communicator;

    @Override
    public void onCreate() {
        super.onCreate();

        communicator = new Communicator(this);

        Log.i("LedNotifyBridge", "Service created.");
    }

    @Override
    public void onDestroy() {
        Log.i("LedNotifyBridge", "Service destroyed.");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (!sbn.getPackageName().equals("com.whatsapp")) {
            return;
        }
        long start = SystemClock.elapsedRealtime();
        communicator.sendRequest(sbn);
        Log.i("LedNotifyBridge", "time: " + (SystemClock.elapsedRealtime() - start));
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("LedNotifyBridge", "Notification removed: " + sbn.getPackageName());
    }
}
