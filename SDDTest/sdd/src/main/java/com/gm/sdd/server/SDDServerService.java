package com.gm.sdd.server;

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
 * Created by 80066158 on 2017-03-30.
 */

public class SDDServerService extends Service implements IOnRemoteDataReceiveListener {
    private static final String TAG = "SDDServerService";

    public static final String INTENT_EXTRA_NAME_SDDDEVICE = "SDDDevice";

    private static final int REMOTE_PORT = SDDConstant.UDP_PORT2;
    private static final int UDP_SERVER_PORT = SDDConstant.UDP_PORT1;

    private SDDDevice device = null;
    private String deviceAliveMsg = null;
    private boolean isUdpServerStarted = false;
    private UdpServer udpServer = null;

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "<onBind> start");
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "<onDestroy> start");
        stopUdpServer();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "<onStartCommand> start");

        device = intent.getParcelableExtra(INTENT_EXTRA_NAME_SDDDEVICE);
        deviceAliveMsg = device.toString();

        sendDeviceAliveMsg(SDDConstant.BROADCAST_ADDR, REMOTE_PORT);
        startUdpServer();

        return super.onStartCommand(intent, flags, startId);
    }

    private void sendDeviceAliveMsg(final String remoteIp, final int remotePort) {
        Log.i(TAG, "<sendDeviceAliveMsg> start");

        new Thread(new Runnable() {
            @Override
            public void run() {
                sendMsg(remoteIp,
                        remotePort,
                        deviceAliveMsg,
                        3);
            }
        }).start();

    }

    private void sendMsg(final String remoteIp,
                         final int remotePort,
                         final String msg,
                         final int sendCount) {
        UdpClient client = new UdpClient(remoteIp, remotePort);
        for (int i = 0; i < sendCount; i++) {
            client.sendMsg(msg);
        }
        client.close();
    }

    private void startUdpServer() {
        Log.i(TAG, "<startUdpServer> isUdpServerStarted = " + isUdpServerStarted);

        if (isUdpServerStarted) {
            return;
        }

        udpServer = new UdpServer(UDP_SERVER_PORT);
        udpServer.setOnRemoteDataReceiveListener(this);
        udpServer.start();
    }

    private void stopUdpServer() {
        Log.i(TAG, "<stopUdpServer> isUdpServerStarted = " + isUdpServerStarted);

        if (!isUdpServerStarted) {
            return;
        }

        if (null != udpServer) {
            udpServer.close();
            udpServer = null;
        }
    }

    @Override
    public void onRemoteDataReceive(String remoteIp, int remotePort, String remoteData) {
        Log.i(TAG, "<onRemoteDataReceive> " + remoteIp + ", " +
                remotePort + ", " + remoteData);
        if (null == remoteData) {
            return;
        }

        String deviceType = getDeviceTypeFromSearchMsg(remoteData);
        Log.i(TAG, "<onRemoteDataReceive> deviceType = " + deviceType);
        if (null == deviceType) {
            return;
        }

        boolean needSendDeviceAliveMsg = false;
        if (deviceType.equals("*") ||
                (device.getDeviceType() != null &&
                    device.getDeviceType().equals(deviceType))) {
            needSendDeviceAliveMsg = true;
        }

        if (needSendDeviceAliveMsg) {
            sendDeviceAliveMsg(remoteIp, remotePort);
        }
    }

    private String getDeviceTypeFromSearchMsg(String searchMsg) {
        Log.i(TAG, "<getDeviceTypeFromSearchMsg> searchMsg = " + searchMsg);
        if (null == searchMsg) {
            return null;
        }

        if (!searchMsg.startsWith(SDDConstant.SEARCH_DEVICE_CMD_HEAD)) {
            return null;
        }

        String deviceType = "*";
        if (searchMsg.length() > SDDConstant.SEARCH_DEVICE_CMD_HEAD.length()) {
            deviceType = searchMsg.substring(SDDConstant.SEARCH_DEVICE_CMD_HEAD.length());
        }
        return deviceType;
    }
}
