package com.tricora.lednotifybridge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Tille on 29.12.2015.
 */
public class Communicator {

    private Context ctx;
    private boolean enabled;


    public Communicator(Context ctx) {
        this.ctx = ctx;

        enabled = isHomeWLAN();
    }

//    @Override
//    public void onReceive(Context context, Intent intent) {
//        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
//        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
//            enabled = isHomeWLAN();
//            Toast.makeText(ctx, "Home WLAN connected", Toast.LENGTH_LONG).show();
//        } else {
//            enabled = false;
//            Toast.makeText(ctx, "Home WLAN gone", Toast.LENGTH_LONG).show();
//        }
//
//        Log.i("LedNotifyBridge", "connection changed: " + enabled);
//
//    }

    public void sendRequest(StatusBarNotification sbn) {

        Log.i("LedNotifyBridge", "" + isHomeWLAN());
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL("http://192.168.0.100:9000/posted");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(3000);
            //urlConnection.setRequestMethod("POST");
            InputStream in = urlConnection.getInputStream();



        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
    }

    public boolean isHomeWLAN() {
        WifiManager wifiManager = (WifiManager)ctx.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return (wifiInfo.getSSID().equals("\"tp router\""));
    }
}
