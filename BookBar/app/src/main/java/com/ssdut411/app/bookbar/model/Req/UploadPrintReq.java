package com.ssdut411.app.bookbar.model.Req;

import com.ssdut411.app.bookbar.activity.show.WifiInfo;

import java.util.List;

/**
 * Created by LENOVO on 2016/11/5.
 */
public class UploadPrintReq extends BaseReq {
    private String locationX;
    private String locationY;
    private String wifiInfoList;

    public UploadPrintReq() {
    }

    public String getLocationX() {
        return locationX;
    }

    public void setLocationX(String locationX) {
        this.locationX = locationX;
    }

    public String getLocationY() {
        return locationY;
    }

    public void setLocationY(String locationY) {
        this.locationY = locationY;
    }

    public String getWifiInfoList() {
        return wifiInfoList;
    }

    public void setWifiInfoList(String wifiInfoList) {
        this.wifiInfoList = wifiInfoList;
    }
}
