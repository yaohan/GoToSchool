package com.ssdut411.app.bookbar.activity.show;

/**
 * Created by yao_han on 2016/10/21.
 */
public class WifiInfo {
    private String bssid;
    private int level;

    public WifiInfo(String bssid, int level) {
        this.bssid = bssid;
        this.level = level;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
