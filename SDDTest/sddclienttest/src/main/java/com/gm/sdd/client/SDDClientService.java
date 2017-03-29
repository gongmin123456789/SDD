package com.gm.sdd.client;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gm.sdd.udp.UdpClient;

/**
 * Created by 80066158 on 2017-03-17.
 */

public class SDDClientService extends Service {
    private static final String TAG = "SDDClientService";

    public static final String INTENT_EXTRA_NAME_SEARCH_TYPE = "searchType";
    public static final String INTENT_EXTRA_NAME_ON_DEVICE_CHANGE_LISTENER = "onDeviceChangeListener";

    private static final String SEARCH_DEVICE_CMD_HEAD = "SDD_SEARCH:";
    private static final String BROADCAST_ADDR = "255.255.255.255";
    private static final int REMOTE_PORT = 8888;
    private static final int SEARCH_PERIOD = 5000; // 5000ms

    private String searchType = null;   // the deviceType of device which will be search
    private IOnDeviceChangeListener onDeviceChangeListener = null;
    private boolean isSearchStarted = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "<onBind> start");
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "<onCreate> start");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "<onDestroy> start");
        super.onDestroy();
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i(TAG, "<onRebind> start");
        super.onRebind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "<onStartCommand> start");
        searchType = intent.getStringExtra(INTENT_EXTRA_NAME_SEARCH_TYPE);
        onDeviceChangeListener = intent.getParcelableExtra(INTENT_EXTRA_NAME_ON_DEVICE_CHANGE_LISTENER);
        startSearchDevice();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "<onUnbind> start");
        return super.onUnbind(intent);
    }

    private void startSearchDevice() {
        Log.i(TAG, "<startSearchDevice> start");

        isSearchStarted = true;
        final String searchCmd = SEARCH_DEVICE_CMD_HEAD + searchType;
        final UdpClient udpClient = new UdpClient(BROADCAST_ADDR, REMOTE_PORT);
        final int count = 3;

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "<startSearchDevice:run> start");

                while (isSearchStarted) {
                    for (int i = 0; i < count; i++) {
                        udpClient.sendMsg(searchCmd);
                    }

                    try {
                        Thread.sleep(SEARCH_PERIOD);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
