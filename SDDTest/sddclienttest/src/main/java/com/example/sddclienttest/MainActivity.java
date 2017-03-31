package com.example.sddclienttest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.gm.sdd.client.SDDDeviceManager;
import com.gm.sdd.client.IOnDeviceChangeListener;
import com.gm.sdd.client.SDDClient;
import com.gm.sdd.common.SDDDevice;

public class MainActivity extends AppCompatActivity implements IOnDeviceChangeListener {

    private static final String TAG = "MainActivity";

    private ListView deviceListView = null;
    private DeviceListViewAdapter deviceListViewAdapter = null;
    private SDDClient sddClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initContent();
        startSDDClient();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "<onDestroy> start");

        stopSDDClient();
        super.onDestroy();
    }

    private void initContent() {
        Log.i(TAG, "<initContent> start");

        deviceListView = (ListView) findViewById(R.id.deviceListView);
        deviceListViewAdapter = new DeviceListViewAdapter(this, null);
        deviceListView.setAdapter(deviceListViewAdapter);
    }

    private void startSDDClient() {
        Log.i(TAG, "<startSDDClient> start");

        sddClient = new SDDClient(this, "*");
        sddClient.setOnDeviceChangeListener(this);
        sddClient.start();
    }

    private void stopSDDClient() {
        Log.i(TAG, "<stopSDDClient> start");

        if (null != sddClient) {
            sddClient.stop();
            sddClient = null;
        }
    }

    @Override
    public void onDeviceOnLine(SDDDevice device) {
        Log.i(TAG, "<onDeviceOnLine> " + device.toString());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                deviceListViewAdapter.setDeviceList(sddClient.getDeviceList());
            }
        });
    }

    @Override
    public void onDeviceOffLine(SDDDevice device) {
        Log.i(TAG, "<onDeviceOffLine> " + device.toString());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                deviceListViewAdapter.setDeviceList(sddClient.getDeviceList());
            }
        });
    }
}
