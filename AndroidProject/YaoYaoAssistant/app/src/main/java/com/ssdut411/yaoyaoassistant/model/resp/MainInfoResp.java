package com.ssdut411.yaoyaoassistant.model.resp;

import com.ssdut411.yaoyaoassistant.model.MainInfo;

/**
 * Created by yao_han on 2017/4/22.
 */
public class MainInfoResp extends BaseResp {
    private MainInfo data;

    public MainInfoResp() {
    }

    public MainInfo getData() {
        return data;
    }

    public void setData(MainInfo data) {
        this.data = data;
    }
}
