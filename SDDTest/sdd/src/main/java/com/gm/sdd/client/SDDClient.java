package com.gm.sdd.client;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.gm.sdd.common.IOnRemoteDataReceiveListener;
import com.gm.sdd.common.SDDConstant;
import com.gm.sdd.common.SDDDevice;
import com.gm.sdd.udp.UdpClient;
import com.gm.sdd.udp.UdpServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 80066158 on 2017-03-17.
 */

public class SDDClient implements IOnRemoteDataReceiveListener {
    private static final String TAG = "SDDClient";

    private static final int REMOTE_PORT = SDDConstant.UDP_PORT1;
    private static final int UDP_SERVER_PORT = SDDConstant.UDP_PORT2;
    private static final int SEARCH_PERIOD = 5000; // 5000ms

    private Context context = null;
    private String searchType = null;   // the deviceType of device which will be search
    private boolean isStarted = false;
    private boolean isSearchStarted = false;
    private UdpServer udpServer = null;

    public SDDClient(Context context, String searchType) {
        this.context = context;
        this.searchType = searchType;
    }

    public void setOnDeviceChangeListener(IOnDeviceChangeListener onDeviceChangeListener) {
        SDDDeviceManager.setOnDeviceChangeListener(onDeviceChangeListener);
    }

    public void start() {
        Log.i(TAG, "<start> isStarted = " + isStarted);

        if (isStarted) {
            return;
        }

        // init data
        isStarted = true;

        startUdpServer();
        startSearchDevice();
    }

    public void stop() {
        Log.i(TAG, "<stop> isStarted = " + isStarted);

        if (!isStarted) {
            return;
        }

        // clear data
        isStarted = false;

        stopSearchDevice();
        stopUdpServer();
    }

    public void restart() {
        Log.i(TAG, "<restart> isStarted = " + isStarted);

        if (!isStarted) {
            // if not start, start it
            start();
        }
    }

    public boolean isStarted() {
        return isStarted;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        Log.i(TAG, "<setSearchType> searchType = " + searchType);
        this.searchType = searchType;
    }

    public List<SDDDevice> getDeviceList() {
        return SDDDeviceManager.getDeviceList();
    }

    private void startUdpServer() {
        Log.i(TAG, "<startUdpServer> start");

        udpServer = new UdpServer(UDP_SERVER_PORT);
        udpServer.setOnRemoteDataReceiveListener(this);
        udpServer.start();
    }

    private void stopUdpServer() {
        Log.i(TAG, "<stopUdpServer> start");

        if (null != udpServer) {
            udpServer.close();
            udpServer = null;
        }
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

    @Override
    public void onRemoteDataReceive(String remoteIp, int remotePort, String remoteData) {
        Log.i(TAG, "<onRemoteDataReceive> " + remoteIp + ", "
                + remotePort + ", " + remoteData);
        try {
            JSONObject jsonObject = new JSONObject(remoteData);

            String msgType = jsonObject.getString("msgType");
            if (null == msgType) {
                return;
            }
            Log.i(TAG, "<onRemoteDataReceive> msgType = " + msgType);
            if (msgType.equals(SDDConstant.MSG_TYPE_ON_LINE)) {
                SDDDevice device = SDDDevice.parse(jsonObject);
                SDDDeviceManager.addDevice(device);
            } else if (msgType.equals(SDDConstant.MSG_TYPE_OFF_LINE)) {
                SDDDevice device = SDDDevice.parse(jsonObject);
                SDDDeviceManager.removeDevice(device);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
