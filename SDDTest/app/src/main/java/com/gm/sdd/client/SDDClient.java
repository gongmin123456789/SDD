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

public class SDDClient implements IOnDeviceChangeListener, Parcelable {
    private static final String TAG = "SDDClient";

    private Context context = null;
    private String searchType = null;   // the deviceType of device which will be search
    private IOnDeviceChangeListener onDeviceChangeListener = null;
    private boolean isStarted = false;
    private List<SDDDevice> deviceList = null;

    public SDDClient(Context context, String searchType) {
        this.context = context;
        this.searchType = searchType;
    }

    public void setOnDeviceChangeListener(IOnDeviceChangeListener onDeviceChangeListener) {
        this.onDeviceChangeListener = onDeviceChangeListener;
    }

    public void start() {
        Log.i(TAG, "<start> isStarted = " + isStarted);

        if (isStarted) {
            return;
        }

        // init data
        isStarted = true;
        deviceList = new ArrayList<SDDDevice>();

        // start service
        Intent intent = new Intent(context, SDDClientService.class);
        intent.putExtra(SDDClientService.INTENT_EXTRA_NAME_SEARCH_TYPE, searchType);
        intent.putExtra(SDDClientService.INTENT_EXTRA_NAME_ON_DEVICE_CHANGE_LISTENER, this);
        context.startService(intent);
    }

    public void stop() {
        Log.i(TAG, "<stop> isStarted = " + isStarted);

        if (!isStarted) {
            return;
        }

        // clear data
        isStarted = false;
        deviceList = new ArrayList<SDDDevice>();

        // stop service
        Intent intent = new Intent(context, SDDClientService.class);
        context.stopService(intent);
    }

    public void restart() {
        Log.i(TAG, "<restart> isStarted = " + isStarted);

        if (!isStarted) {
            // if not start, start it
            start();
        } else {
            // if started, just clear deviceList
            deviceList = new ArrayList<SDDDevice>();
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
        return deviceList;
    }

    @Override
    public void onDeviceChange(final SDDDevice newAddedDevice) {
        Log.i(TAG, "<onDeviceChange> " + newAddedDevice.toString());
        if (null == onDeviceChangeListener) {
            Log.w(TAG, "<onDeviceChange> onDeviceChangeListener is null");
            return;
        }

        onDeviceChangeListener.onDeviceChange(newAddedDevice);
    }


    protected SDDClient(Parcel in) {
        searchType = in.readString();
    }

    public static final Creator<SDDClient> CREATOR = new Creator<SDDClient>() {
        @Override
        public SDDClient createFromParcel(Parcel in) {
            return new SDDClient(in);
        }

        @Override
        public SDDClient[] newArray(int size) {
            return new SDDClient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(searchType);
    }
}
