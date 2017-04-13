package com.ssdut411.app.bookbarstatic.activity.show;

/**
 * Created by LENOVO on 2016/10/23.
 */
public class BookInfo {
    private String bookName;
    private Location bookLocation;

    public BookInfo(String bookName, Location bookLocation) {
        this.bookName = bookName;
        this.bookLocation = bookLocation;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Location getBookLocation() {
        return bookLocation;
    }

    public void setBookLocation(Location bookLocation) {
        this.bookLocation = bookLocation;
    }
}
