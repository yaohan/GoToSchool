package com.ssdut411.app.bookbar.activity.show;

import java.util.List;

/**
 * Created by yao_han on 2016/10/20.
 */
public class Location {
    private float x;
    private float y;
    private String location;
    private List<WifiInfo> wifiInfo;

    public Location(float x, float y, List<WifiInfo> wifiInfo) {
        this.x = x;
        this.y = y;
        this.wifiInfo = wifiInfo;
    }

    public Location(String location, List<WifiInfo> wifiInfo) {
        this.location = location;
        this.wifiInfo = wifiInfo;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<WifiInfo> getWifiInfo() {
        return wifiInfo;
    }

    public void setWifiInfo(List<WifiInfo> wifiInfo) {
        this.wifiInfo = wifiInfo;
    }

}
