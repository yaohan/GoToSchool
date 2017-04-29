package com.ssdut411.yaoyaoassistant.model;

import java.util.List;

/**
 * Created by yao_han on 2017/4/22.
 */
public class MainInfo {
    private double money;
    private List<Account> list;

    public MainInfo() {
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public List<Account> getList() {
        return list;
    }

    public void setList(List<Account> list) {
        this.list = list;
    }
}
