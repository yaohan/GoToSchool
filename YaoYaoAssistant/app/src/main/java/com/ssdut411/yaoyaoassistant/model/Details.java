package com.ssdut411.yaoyaoassistant.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by yao_han on 2017/4/22.
 */
public class Details {
    private int id;
    private int accountId;
    private int tagId;
    private int type;
    private String name;
    private double money;
    private String time;
    private double sum;

    public Details() {
    }

    public Details(String name, double money, String time, double sum) {
        this.name = name;
        this.money = money;
        this.time = time;
        this.sum = sum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
