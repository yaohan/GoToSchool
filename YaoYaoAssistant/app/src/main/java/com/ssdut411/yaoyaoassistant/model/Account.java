package com.ssdut411.yaoyaoassistant.model;

/**
 * Created by yao_han on 2017/4/22.
 */
public class Account {
    private int id;
    private String name;
    private Double money;
    private String desc;

    public Account() {
    }

    public Account(String name, Double money) {
        this.name = name;
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
