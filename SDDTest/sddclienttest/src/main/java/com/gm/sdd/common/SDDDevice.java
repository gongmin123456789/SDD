package com.gm.sdd.common;

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
