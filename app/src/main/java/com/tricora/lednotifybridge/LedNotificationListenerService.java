package com.tricora.lednotifybridge;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Tille on 29.12.2015.
 */
public class LedNotificationListenerService extends NotificationListenerService implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String SERVER_IP = "server_ip";
    private static final String SERVER_PORT = "server_port";
    private static final String SERVER_TIMEOUT = "server_timeout";
    private static final String PACKAGE_LIST = "key_allowed_packages";

    private Communicator communicator;
    private SharedPreferences prefs;

    private Set<String> packageList;


    @Override
    public void onCreate() {
        super.onCreate();


        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        communicator = new Communicator(this);
        updateTimeout();
        updatePort();
        updateIp();
        updatePackageList();
        Log.i("LedNotifyBridge", "Service created.");
    }

    @Override
    public void onDestroy() {
        prefs.unregisterOnSharedPreferenceChangeListener(this);
        Log.i("LedNotifyBridge", "Service destroyed.");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i("LedNotifyBridge", "notification received: " + sbn.getPackageName());
        if (!packageList.contains(sbn.getPackageName())) {
            return;
        }
        communicator.sendRequest(sbn);

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("LedNotifyBridge", "Notification removed: " + sbn.getPackageName());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case SERVER_IP:
                updateIp();
                break;
            case SERVER_PORT:
                updatePort();
                break;
            case SERVER_TIMEOUT:
                updateTimeout();
                break;
            case PACKAGE_LIST:
                updatePackageList();
                break;
            default:
        }
    }

    private void updateIp() {
        communicator.setServerIP(prefs.getString(SERVER_IP, "192.168.0.100"));
    }

    private void updatePort() {
        communicator.setPort(Integer.valueOf(prefs.getString(SERVER_PORT, "9000")));
    }

    private void updateTimeout() {
        communicator.setTimeout(Integer.valueOf(prefs.getString(SERVER_TIMEOUT, "3000")));
    }

    private void updatePackageList() {
        packageList = prefs.getStringSet(getResources().getString(R.string.pref_key_allowed_packages), new HashSet<String>());
        Log.i("LedNotifyBridge", "package list changed - number of entries: " + packageList.size());
    }
}
