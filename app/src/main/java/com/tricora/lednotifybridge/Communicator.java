package com.tricora.lednotifybridge;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Tille on 29.12.2015.
 */
public class Communicator {

    private Context ctx;
    private boolean enabled;

    private String serverIP = "192.168.0.100";
    private int port = 9000;
    private int timeout = 3000;

    public Communicator(Context ctx) {
        this.ctx = ctx;
        enabled = isHomeWLAN();
    }

    public Communicator(Context ctx, String ip, int port) {
        this.ctx = ctx;
        enabled = isHomeWLAN();

        this.serverIP = ip;
        this.port = port;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
        Log.i("LedNotifyBridge", "IP changed: " + serverIP);
    }

    public void setPort(int port) {
        this.port = port;
        Log.i("LedNotifyBridge", "Port changed: " + port);
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
        Log.i("LedNotifyBridge", "timeout changed: " + timeout);
    }

    public void sendRequest(StatusBarNotification sbn) {

        Log.i("LedNotifyBridge", "" + isHomeWLAN());
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL("http://" + serverIP + ":" + port + "/posted");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(timeout);
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
