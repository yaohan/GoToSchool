package com.ssdut411.app.bookbar.model.Req;

/**
 * Created by LENOVO on 2016/11/5.
 */
public class GetBookByISBNReq extends BaseReq {
    private String isbn;

    public GetBookByISBNReq() {
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
