package com.ssdut411.app.bookbar.model;

/**
 * Created by LENOVO on 2016/10/29.
 */
public class Library {
    private int img;
    private String name;

    public Library() {
    }

    public Library(int img, String name) {
        this.img = img;
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
