package com.tricora.lednotifybridge;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Tille on 29.12.2015.
 */
public class LedNotificationListenerService extends NotificationListenerService implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Communicator communicator;
    private SharedPreferences prefs;


    @Override
    public void onCreate() {
        super.onCreate();

        communicator = new Communicator(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);


        Log.i("LedNotifyBridge", "Service created.");
    }

    @Override
    public void onDestroy() {
        prefs.unregisterOnSharedPreferenceChangeListener(this);
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Toast.makeText(this, key + "  ->  " + sharedPreferences.getString(key, "not found"), Toast.LENGTH_LONG).show();
    }
}
