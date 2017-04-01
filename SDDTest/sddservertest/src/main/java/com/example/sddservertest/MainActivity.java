package com.example.sddservertest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.gm.sdd.common.SDDDevice;
import com.gm.sdd.server.SDDServer;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final String DEVICE_TYPE = "SDDDeviceTest";

    private SDDServer sddServer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "<onCreate> start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startSDDServer();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "<onDestroy> start");
        stopSDDServer();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "<onBackPressed> start");
        finish();
    }

    private void startSDDServer() {
        Log.i(TAG, "<startSDDServer> start");

        SDDDevice device = new SDDDevice();
        device.setDeviceType(DEVICE_TYPE);
        device.setName("SDDDevice1");
        device.setPort(0);
        device.setIconUrl("");
        device.setMac("macAddr");
        device.setIp("ipAddr");
        device.setUuid("1234567890");
        sddServer = new SDDServer(this, device);
        sddServer.start();
    }

    private void stopSDDServer() {
        Log.i(TAG, "<stopSDDServer> start");

        if (null != sddServer) {
            sddServer.stop();
            sddServer = null;
        }
    }
}
