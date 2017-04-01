package com.gm.sdd.client;

import android.util.Log;

import com.gm.sdd.common.SDDConstant;
import com.gm.sdd.common.SDDDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 80066158 on 2017-03-31.
 */

public class SDDDeviceManager {
    private static final String TAG = "SDDDeviceManager";

    private static List<SDDDevice> deviceList = null;
    private static IOnDeviceChangeListener onDeviceChangeListener = null;

    public static void setOnDeviceChangeListener(IOnDeviceChangeListener onDeviceChangeListener) {
        SDDDeviceManager.onDeviceChangeListener = onDeviceChangeListener;
    }

    public static void addDevice(SDDDevice device) {
        synchronized (SDDDeviceManager.class) {
            if (null == device) {
                Log.w(TAG, "<addDevice> device is null");
                return;
            }

            if (null == deviceList) {
                deviceList = new ArrayList<SDDDevice>();
            }

            if (null == device.getUuid()) {
                Log.e(TAG, "<addDevice> " + device.toString() + ", uuid is null");
                return;
            }

            if (isDeviceExist(device)) {
                Log.w(TAG, "<addDevice> " + device.toString() + "-----already exist");
                SDDDevice tempDevice = getDevice(device.getUuid());
                restartDeviceExpirationTimer(tempDevice);
                return;
            }

            deviceList.add(device);
            restartDeviceExpirationTimer(device);
        }

        if (null != onDeviceChangeListener) {
            onDeviceChangeListener.onDeviceOnLine(device);
        }
    }

    public static void removeDevice(SDDDevice device) {
        synchronized (SDDDeviceManager.class) {
            if (null == device) {
                Log.w(TAG, "<removeDevice> device is null");
                return;
            }

            if (!isDeviceExist(device)) {
                Log.w(TAG, "<removeDevice> " + device.toString() + " is not exist");
                return;
            }

            removeDeviceByUuid(device.getUuid());
        }

        if (null != onDeviceChangeListener) {
            onDeviceChangeListener.onDeviceOffLine(device);
        }
    }

    public synchronized static void removeDevice(String uuid) {
        removeDeviceByUuid(uuid);
    }

    public synchronized static List<SDDDevice> getDeviceList() {
        if (null == deviceList) {
            return null;
        }

        return new ArrayList<>(deviceList);
    }

    private static void removeDeviceByUuid(String uuid) {
        if (null == uuid) {
            Log.w(TAG, "<removeDeviceByUuid> uuid is null");
            return;
        }
        if (null == deviceList) {
            return;
        }

        for (SDDDevice device : deviceList) {
            if (device.getUuid().equals(uuid)) {
                deviceList.remove(device);
            }
        }
    }

    private static boolean isDeviceExist(SDDDevice device) {
        if (null == deviceList) {
            return false;
        }

        for (SDDDevice sddDevice : deviceList) {
            if (device.getUuid().equals(sddDevice.getUuid())) {
                return true;
            }
        }

        return false;
    }

    private static SDDDevice getDevice(String uuid) {
        if (null == uuid ||
                null == deviceList) {
            return null;
        }

        for (SDDDevice sddDevice : deviceList) {
            if (uuid.equals(sddDevice.getUuid())) {
                return sddDevice;
            }
        }

        return null;
    }

    private static void restartDeviceExpirationTimer(final SDDDevice device) {
        if (null == device) {
            Log.w(TAG, "<restartDeviceExpirationTimer> device is null");
            return;
        }

        Timer timer = device.getTimer();
        if (null != timer) {
            timer.cancel();
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i(TAG, "<restartDeviceExpirationTimer:run> " + device.toString());
                removeDevice(device);
            }
        }, SDDConstant.SDD_DEVICE_EXPIRATION_TIME * 1000);

        device.setTimer(timer);
    }
}
