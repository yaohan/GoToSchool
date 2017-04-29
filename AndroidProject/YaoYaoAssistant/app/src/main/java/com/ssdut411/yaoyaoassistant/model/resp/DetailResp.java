package com.ssdut411.yaoyaoassistant.model.resp;

import com.ssdut411.yaoyaoassistant.model.Details;

import java.util.List;

/**
 * Created by yao_han on 2017/4/23.
 */
public class DetailResp extends BaseResp{
    private List<Details> data;

    public DetailResp() {
    }

    public List<Details> getData() {
        return data;
    }

    public void setData(List<Details> data) {
        this.data = data;
    }
}
