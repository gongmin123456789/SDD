package com.gm.sdd.common;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 80066158 on 2017-03-17.
 */

public class SDDDevice {
    private String uuid = null;
    private String name = null;
    private String ip = null;
    private String mac = null;
    private String iconUrl = null;
    private int port = 0;
    private String deviceType = null;

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

    public static SDDDevice parse(String jsonStr) {
        if (null == jsonStr) {
            return null;
        }

        SDDDevice device = new SDDDevice();

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            device.setUuid(jsonObject.getString("uuid"));
            device.setName(jsonObject.getString("name"));
            device.setIp(jsonObject.getString("ip"));
            device.setMac(jsonObject.getString("mac"));
            device.setIconUrl(jsonObject.getString("iconUrl"));
            device.setPort(jsonObject.getInt("port"));
            device.setDeviceType(jsonObject.getString("deviceType"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return device;
    }

    @Override
    public String toString() {
        String str = "";
        str += "uuid = " + uuid + ", ";
        str += "name = " + name + ", ";
        str += "ip = " + ip + ", ";
        str += "mac = " + mac + ", ";
        str += "iconUrl = " + iconUrl + ", ";
        str += "port = " + port + ", ";
        str += "deviceType = " + deviceType;
        return str;
    }
}
