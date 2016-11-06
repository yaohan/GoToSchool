package com.ssdut411.app.bookbar.model.Resp;

/**
 * Created by LENOVO on 2016/11/5.
 */
public class GetBookLocationResp extends BaseResp {
    private String locationX;
    private String locationY;

    public GetBookLocationResp() {
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
