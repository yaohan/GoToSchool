package com.ssdut411.app.bookbar.model.Resp;

import com.ssdut411.app.bookbar.model.BookModel;

/**
 * Created by LENOVO on 2016/11/5.
 */
public class GetBookByISBNResp extends BaseResp {
    private BookModel book;

    public GetBookByISBNResp() {
    }

    public BookModel getBook() {
        return book;
    }

    public void setBook(BookModel book) {
        this.book = book;
    }
}
