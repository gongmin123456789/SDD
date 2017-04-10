package com.gm.sdd.common;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;

/**
 * Created by 80066158 on 2017-03-17.
 */

public class SDDDevice implements Parcelable {
    private static final String UUID = "uuid";
    private static final String NAME = "name";
    private static final String IP = "ip";
    private static final String MAC = "mac";
    private static final String ICON_URL = "iconUrl";
    private static final String PORT = "port";
    private static final String DEVICE_TYPE = "deviceType";

    private String uuid = null;
    private String name = null;
    private String ip = null;
    private String mac = null;
    private String iconUrl = null;
    private int port = 0;
    private String deviceType = null;
    private Timer timer = null; // this time is use to auto delete this device

    public SDDDevice() {}

    protected SDDDevice(Parcel in) {
        uuid = in.readString();
        name = in.readString();
        ip = in.readString();
        mac = in.readString();
        iconUrl = in.readString();
        port = in.readInt();
        deviceType = in.readString();
    }

    public static final Creator<SDDDevice> CREATOR = new Creator<SDDDevice>() {
        @Override
        public SDDDevice createFromParcel(Parcel in) {
            return new SDDDevice(in);
        }

        @Override
        public SDDDevice[] newArray(int size) {
            return new SDDDevice[size];
        }
    };

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public static SDDDevice parse(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }

        SDDDevice device = new SDDDevice();

        try {
            device.setUuid(jsonObject.getString(UUID));
            device.setName(jsonObject.getString(NAME));
            device.setIp(jsonObject.getString(IP));
            device.setMac(jsonObject.getString(MAC));
            device.setIconUrl(jsonObject.getString(ICON_URL));
            device.setPort(jsonObject.getInt(PORT));
            device.setDeviceType(jsonObject.getString(DEVICE_TYPE));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return device;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = toJSON();
        String str = jsonObject.toString();
        return str;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uuid);
        parcel.writeString(name);
        parcel.writeString(ip);
        parcel.writeString(mac);
        parcel.writeString(iconUrl);
        parcel.writeInt(port);
        parcel.writeString(deviceType);
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(UUID, uuid);
            jsonObject.put(NAME, name);
            jsonObject.put(IP, ip);
            jsonObject.put(MAC, mac);
            jsonObject.put(ICON_URL, iconUrl);
            jsonObject.put(PORT, port);
            jsonObject.put(DEVICE_TYPE, deviceType);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return jsonObject;
    }
}
