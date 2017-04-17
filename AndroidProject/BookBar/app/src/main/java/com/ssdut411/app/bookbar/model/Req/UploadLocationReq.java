package com.ssdut411.app.bookbar.model.Req;

import com.ssdut411.app.bookbar.activity.show.Location;

/**
 * Created by LENOVO on 2016/11/5.
 */
public class UploadLocationReq extends BaseReq {
    private Location location;

    public UploadLocationReq() {
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
