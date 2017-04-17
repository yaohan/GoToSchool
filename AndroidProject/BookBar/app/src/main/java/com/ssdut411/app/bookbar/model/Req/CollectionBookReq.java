package com.ssdut411.app.bookbar.model.Req;

import java.util.Date;

/**
 * Created by LENOVO on 2016/11/5.
 */
public class CollectionBookReq extends BaseReq {
    private int bookId;
    private String userId;
    private String time;
    public CollectionBookReq() {
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
}
