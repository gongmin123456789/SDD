package com.example.sddservertest;

import android.app.ActivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.gm.sdd.common.SDDDevice;
import com.gm.sdd.server.SDDServer;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final String DEVICE_TYPE = "SDDDeviceTest";

    private SDDServer sddServer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "<onCreate> start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MyApplication) getApplication()).addActivity(this);
        startSDDServer();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "<onDestroy> start");
        stopSDDServer();

        ((MyApplication) getApplication()).exit();

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "<onBackPressed> start");
        finish();
    }

    private void startSDDServer() {
        Log.i(TAG, "<startSDDServer> start");

        sddServer = new SDDServer(this);
        sddServer.setIcon(R.mipmap.ic_launcher);
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
