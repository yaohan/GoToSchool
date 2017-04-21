package com.ssdut411.app.bookbar.model.Req;

/**
 * Created by LENOVO on 2016/11/5.
 */
public class GetBookLocationReq extends BaseReq {
    private String bookId;

    public GetBookLocationReq() {
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
