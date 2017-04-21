package com.ssdut411.app.bookbar.model.Req;

/**
 * Created by LENOVO on 2016/11/5.
 */
public class BorrowBookReq extends BaseReq {
    private int bookId;
    private String userId;
    private String time;
    private float locationX;
    private float locationY;
    public BorrowBookReq() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getLocationX() {
        return locationX;
    }

    public void setLocationX(float locationX) {
        this.locationX = locationX;
    }

    public float getLocationY() {
        return locationY;
    }

    public void setLocationY(float locationY) {
        this.locationY = locationY;
    }
}
