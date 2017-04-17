package com.ssdut411.app.bookbarstatic.activity.system;

import android.app.Application;
import android.content.Context;

import com.ssdut411.app.bookbarstatic.activity.show.DBFile;
import com.ssdut411.app.bookbarstatic.utils.L;
import com.ssdut411.app.bookbarstatic.utils.SPUtils;
import com.ssdut411.app.bookbarstatic.volley.VolleyManager;


/**
 * Created by yao_han on 2015/12/22.
 */
public class MainApplication extends Application {
    // 单例一个MainApplication
    private static MainApplication instance;

    private String userId, phoneNumber;
    private boolean theme;
    private Context context;
    private boolean hasScan = false;
    private boolean isAdmin = false;
    private String server;
    private float locationX,locationY;
    private boolean directBorrow = false;
    /**
     * 得到MainApplication实例
     *
     * @return
     */
    public static MainApplication getInstance() {
        return MainApplication.instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DBFile.getInstance().init(getApplicationContext());
        instance = this;
        context = getApplicationContext();
        // 打印日志信息
        L.isDebug = true;
        VolleyManager.init(getApplicationContext());

//        userId = SPUtils.get(context, "userId", "").toString();
//        if (userId.length() == 0) {
//            userId = null;
//        }
        userId = "1";
        phoneNumber = "18840824301";

//        phoneNumber = SPUtils.get(context, "phoneNumber", "").toString();
//        if (phoneNumber.length() == 0) {
//            phoneNumber = null;
//        }
        server = SPUtils.get(context,"server","").toString();
        if(server.length() == 0){
            server = "http://192.168.1.104:8081";
        }
    }

    public void clear() {
        userId = null;
        phoneNumber = null;
        SPUtils.remove(context,"userId");
    }

    public boolean getTheTheme() {
        return theme;
    }

    public void setTheme(Boolean theme) {
        this.theme = theme;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        SPUtils.put(context, "userId", userId);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        SPUtils.put(context, "phoneNumber", phoneNumber);
    }

    public boolean isHasScan() {
        return hasScan;
    }

    public void setHasScan(boolean hasScan) {
        this.hasScan = hasScan;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public float getLocationY() {
        return locationY;
    }

    public void setLocationY(float locationY) {
        this.locationY = locationY;
    }

    public float getLocationX() {
        return locationX;
    }

    public void setLocationX(float locationX) {
        this.locationX = locationX;
    }

    public boolean isDirectBorrow() {
        return directBorrow;
    }

    public void setDirectBorrow(boolean directBorrow) {
        this.directBorrow = directBorrow;
    }
}
