package com.gm.sdd.server;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import com.gm.sdd.common.IOnRemoteDataReceiveListener;
import com.gm.sdd.common.SDDConstant;
import com.gm.sdd.common.SDDDevice;
import com.gm.sdd.http.MyHttpServer;
import com.gm.sdd.udp.UdpClient;
import com.gm.sdd.udp.UdpServer;

import java.io.IOException;

/**
 * Created by 80066158 on 2017-03-17.
 */

public class SDDServer implements IOnRemoteDataReceiveListener {
    private static final String TAG = "SDDServer";

    private static final String DEFAULT_NAME = "DEFAULT_NAME";
    private static final String DEFAULT_DEVICE_TYPE = "*";
    private static final int DEFAULT_HTTP_SERVER_PORT = 28888;
    private static final int REMOTE_PORT = SDDConstant.UDP_PORT2;
    private static final int UDP_SERVER_PORT = SDDConstant.UDP_PORT1;

    private Context context = null;
    private SDDDevice device = null;
    private boolean isStarted = false;
    private String deviceOnLineMsg = null;
    private String deviceOffLineMsg = null;
    private boolean isUdpServerStarted = false;
    private UdpServer udpServer = null;
    private MyHttpServer httpServer = null;
    private int iconId = 0;

    public SDDServer(Context context) {
        this.context = context;
        this.device = getDefaultDevice();
    }

    public SDDServer(String name, int httpServerPort, String deviceType, int iconId) {
        this.iconId = iconId;
        this.device = getDefaultDevice();
        device.setName(name);
        device.setPort(httpServerPort);
        device.setDeviceType(deviceType);
    }

    public void setName(String serverName) {
        device.setName(serverName);
    }

    public void setHttpServerPort(int port) {
        device.setPort(port);
    }

    public void setIcon(int iconId) {
        this.iconId = iconId;
    }

    public void setDeviceType(String deviceType) {
        device.setDeviceType(deviceType);
    }

    public void start() {
        Log.i(TAG, "<start> isStarted = " + isStarted);

        if (isStarted) {
            return;
        }
        isStarted = true;

        String deviceStr = device.toString();
        deviceOnLineMsg = "{\"msgType\":\"" +
                SDDConstant.MSG_TYPE_ON_LINE + "\"," +
                deviceStr.substring(1);
        deviceOffLineMsg = "{\"msgType\":\"" +
                SDDConstant.MSG_TYPE_OFF_LINE + "\"," +
                deviceStr.substring(1);

        sendDeviceOnLineMsg(SDDConstant.BROADCAST_ADDR, REMOTE_PORT);
        startUdpServer();

        startHttpServer();
    }

    public void stop() {
        Log.i(TAG, "<stop> isStarted = " + isStarted);

        if (!isStarted) {
            return;
        }
        isStarted = false;

        stopHttpServer();
        stopUdpServer();
        sendDeviceOffLineMsg(SDDConstant.BROADCAST_ADDR, REMOTE_PORT);
    }

    public boolean isStarted() {
        return isStarted;
    }

    private SDDDevice getDefaultDevice() {
        SDDDevice device = new SDDDevice();
        device.setName(DEFAULT_NAME);
        device.setDeviceType(DEFAULT_DEVICE_TYPE);
        device.setPort(DEFAULT_HTTP_SERVER_PORT);
        device.setIp(getIPAddress(context));
        device.setMac(getMacAddress(context));
        device.setUuid(device.getMac());

        String iconUrl = "http://" + device.getIp() + ":" + device.getPort() + MyHttpServer.getIconUri();
        device.setIconUrl(iconUrl);

        return device;
    }

    private void sendDeviceOnLineMsg(final String remoteIp, final int remotePort) {
        Log.i(TAG, "<sendDeviceOnLineMsg> start");

        new Thread(new Runnable() {
            @Override
            public void run() {
                sendMsg(remoteIp,
                        remotePort,
                        deviceOnLineMsg,
                        3);
            }
        }).start();
    }

    private void sendDeviceOffLineMsg(final String remoteIp, final int remotePort) {
        Log.i(TAG, "<sendDeviceOffLineMsg> start");

        new Thread(new Runnable() {
            @Override
            public void run() {
                sendMsg(remoteIp,
                        remotePort,
                        deviceOffLineMsg,
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

    private void startHttpServer() {
        Log.i(TAG, "<startHttpServer> start");

        httpServer = new MyHttpServer(device.getPort(), context);
        httpServer.setIcon(iconId);
        try {
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopHttpServer() {
        Log.i(TAG, "<stopHttpServer> start");

        if (null == httpServer) {
            Log.w(TAG, "<stopHttpServer> httpServer is null");
            return;
        }

        httpServer.stop();
        httpServer = null;
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
            sendDeviceOnLineMsg(remoteIp, REMOTE_PORT);
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

    private String getIPAddress(Context ctx){
        WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
        return Formatter.formatIpAddress(dhcpInfo.ipAddress);
    }

    private static String getMacAddress(Context context){
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }
}
