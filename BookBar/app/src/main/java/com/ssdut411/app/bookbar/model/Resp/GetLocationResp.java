package com.ssdut411.app.bookbar.model.Resp;

/**
 * Created by LENOVO on 2016/11/5.
 */
public class GetLocationResp extends BaseResp {
    private String locationX;
    private String locationY;

    public GetLocationResp() {
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
}
