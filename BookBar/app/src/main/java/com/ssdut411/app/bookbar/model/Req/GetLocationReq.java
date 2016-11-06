package com.ssdut411.app.bookbar.model.Req;

/**
 * Created by LENOVO on 2016/11/5.
 */
public class GetLocationReq extends BaseReq {
    private String wifiInfoList;

    public GetLocationReq() {
    }

    public String getWifiInfoList() {
        return wifiInfoList;
    }

    public void setWifiInfoList(String wifiInfoList) {
        this.wifiInfoList = wifiInfoList;
    }
}
