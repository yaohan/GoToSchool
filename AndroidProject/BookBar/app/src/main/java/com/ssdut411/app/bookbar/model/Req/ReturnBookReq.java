package com.ssdut411.app.bookbar.model.Req;

/**
 * Created by LENOVO on 2016/11/7.
 */
public class ReturnBookReq extends BaseReq {
    private String borrowId;
    private String bookId;

    public ReturnBookReq() {
    }

    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
