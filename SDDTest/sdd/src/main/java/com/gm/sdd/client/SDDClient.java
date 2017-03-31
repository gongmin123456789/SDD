package com.gm.sdd.client;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.gm.sdd.common.SDDDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 80066158 on 2017-03-17.
 */

public class SDDClient {
    private static final String TAG = "SDDClient";

    private Context context = null;
    private String searchType = null;   // the deviceType of device which will be search
    private boolean isStarted = false;

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

        // start service
        Intent intent = new Intent(context, SDDClientService.class);
        intent.putExtra(SDDClientService.INTENT_EXTRA_NAME_SEARCH_TYPE, searchType);
        context.startService(intent);
    }

    public void stop() {
        Log.i(TAG, "<stop> isStarted = " + isStarted);

        if (!isStarted) {
            return;
        }

        // clear data
        isStarted = false;

        // stop service
        Intent intent = new Intent(context, SDDClientService.class);
        context.stopService(intent);
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
}
