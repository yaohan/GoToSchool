package com.ssdut411.app.bookbar.model;

/**
 * Created by LENOVO on 2016/10/29.
 */
public class Book {
    private String url;
    private String name;
    private String press;//出版社

    public Book() {
    }

    public Book(String url, String name, String press) {
        this.url = url;
        this.name = name;
        this.press = press;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }
}
