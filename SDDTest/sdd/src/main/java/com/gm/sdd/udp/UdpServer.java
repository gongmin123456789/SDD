package com.gm.sdd.udp;

import android.util.Log;

import com.gm.sdd.common.IOnRemoteDataReceiveListener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by 80066158 on 2017-03-29.
 */

public class UdpServer {
    private static final String TAG = "UdpServer";

    private static final int RECV_BUF_MAX_LEN = 1024;

    private int port = 18888;
    private DatagramSocket ds = null;
    private IOnRemoteDataReceiveListener onRemoteDataReceiveListener = null;

    public UdpServer(int port) {
        this.port = port;
        start();
    }

    public void setOnRemoteDataReceiveListener(IOnRemoteDataReceiveListener onRemoteDataReceiveListener) {
        this.onRemoteDataReceiveListener = onRemoteDataReceiveListener;
    }

    public synchronized void start() {
        Log.i(TAG, "<start> start");

        try {
            if (null != ds) {
                Log.w(TAG, "<start> UdpServer is started already");
                return;
            }

            ds = new DatagramSocket(port);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    receiveDataLooper();
                }
            }).start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public synchronized void close() {
        Log.i(TAG, "<close> start");

        if (null == ds) {
            Log.w(TAG, "<close> UdpServer is not started yet");
            return;
        }

        ds.close();
        ds = null;
    }

    private void receiveDataLooper() {
        Log.i(TAG, "<receiveDataLooper> start");

        try {
            while (true) {
                byte[] buf = new byte[RECV_BUF_MAX_LEN];
                final DatagramPacket dp = new DatagramPacket(buf, RECV_BUF_MAX_LEN);
                ds.receive(dp);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        receiveData(dp);
                    }
                }).start();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void receiveData(DatagramPacket dp) {
        Log.i(TAG, "<receiveData> start");

        InetAddress addr = dp.getAddress();
        int port = dp.getPort();
        String data = new String(dp.getData(), 0, dp.getLength());

        if (null != onRemoteDataReceiveListener) {
            onRemoteDataReceiveListener.onRemoteDataReceive(addr.getHostName(),
                    port, data);
        } else {
            Log.w(TAG, "<receiveData> onRemoteDataReceiveListener is null");
        }
    }
}
