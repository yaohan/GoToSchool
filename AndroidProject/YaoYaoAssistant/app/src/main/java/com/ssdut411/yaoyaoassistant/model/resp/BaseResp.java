package com.ssdut411.yaoyaoassistant.model.resp;

/**
 * Created by yao_han on 2017/4/22.
 */
public class BaseResp {
    private Boolean status;
    private String desc;

    public BaseResp() {
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
