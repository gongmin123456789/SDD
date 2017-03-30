package com.gm.sdd.udp;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by 80066158 on 2017-03-17.
 */

public class UdpClient {
    private static final String TAG = "UdpClient";

    private String serverIp = null;
    private int serverPort = 0;
    private DatagramSocket socket = null;
    private InetAddress inetAddress = null;

    public UdpClient(String serverIp, int serverPort) {
        setServerIp(serverIp);
        setServerPort(serverPort);

        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
        try {
            inetAddress = InetAddress.getByName(serverIp);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(final String msg) {
        Log.i(TAG, "<sendMsg> msg = " + msg);

        if (null == msg) {
            return;
        }
        if (null == socket) {
            Log.w(TAG, "<sendMsg> socket is null");
            return;
        }
        if (null == inetAddress) {
            Log.w(TAG, "<sendMsg> inetAddress is null");
            return;
        }

        try {
            DatagramPacket packet = new DatagramPacket(msg.getBytes("UTF-8"),
                    msg.length(), inetAddress, serverPort);

            socket.send(packet);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        Log.i(TAG, "<close> start");

        if (null != socket) {
            socket.close();
            socket = null;
        }
    }
}
