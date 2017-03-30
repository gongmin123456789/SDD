package com.gm.sdd.client;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.gm.sdd.common.IOnRemoteDataReceiveListener;
import com.gm.sdd.common.SDDConstant;
import com.gm.sdd.common.SDDDevice;
import com.gm.sdd.udp.UdpClient;
import com.gm.sdd.udp.UdpServer;

/**
 * Created by 80066158 on 2017-03-17.
 */

public class SDDClientService extends Service {
    private static final String TAG = "SDDClientService";

    public static final String INTENT_EXTRA_NAME_SEARCH_TYPE = "searchType";
    public static final String INTENT_EXTRA_NAME_ON_DEVICE_CHANGE_LISTENER = "onDeviceChangeListener";

    private static final int REMOTE_PORT = SDDConstant.UDP_PORT1;
    private static final int UDP_SERVER_PORT = SDDConstant.UDP_PORT2;
    private static final int SEARCH_PERIOD = 5000; // 5000ms

    private String searchType = null;   // the deviceType of device which will be search
    private IOnDeviceChangeListener onDeviceChangeListener = null;
    private boolean isSearchStarted = false;
    private UdpServer udpServer = null;

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

        stopSearchDevice();
        stopUdpServer();

        super.onRebind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "<onStartCommand> start");
        searchType = intent.getStringExtra(INTENT_EXTRA_NAME_SEARCH_TYPE);
        onDeviceChangeListener = intent.getParcelableExtra(INTENT_EXTRA_NAME_ON_DEVICE_CHANGE_LISTENER);

        startUdpServer();
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
        final String searchCmd = SDDConstant.SEARCH_DEVICE_CMD_HEAD + searchType;
        final UdpClient udpClient = new UdpClient(SDDConstant.BROADCAST_ADDR, REMOTE_PORT);
        final int count = 3;

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "<startSearchDevice:run> start");

                while (isSearchStarted) {
                    for (int i = 0; i < count; i++) {
                        udpClient.sendMsg(searchCmd);
                    }

                    if (isSearchStarted) {
                        try {
                            Thread.sleep(SEARCH_PERIOD);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                udpClient.close();
            }
        }).start();
    }

    private void stopSearchDevice() {
        Log.i(TAG, "<stopSearchDevice> start");

        isSearchStarted = false;
    }

    private void startUdpServer() {
        Log.i(TAG, "<startUdpServer> start");

        udpServer = new UdpServer(UDP_SERVER_PORT);
        udpServer.setOnRemoteDataReceiveListener(new IOnRemoteDataReceiveListener() {
            @Override
            public void onRemoteDataReceive(String remoteIp, int remotePort, String remoteData) {
                Log.i(TAG, "<startUdpServer:onRemoteDataReceive> " + remoteIp + ", "
                    + remotePort + ", " + remoteData);
                SDDDevice device = SDDDevice.parse(remoteData);
                if (null != onDeviceChangeListener &&
                        null != device) {
                    onDeviceChangeListener.onDeviceChange(device);
                }
            }
        });
        udpServer.start();
    }

    private void stopUdpServer() {
        Log.i(TAG, "<stopUdpServer> start");

        if (null != udpServer) {
            udpServer.close();
            udpServer = null;
        }
    }
}
