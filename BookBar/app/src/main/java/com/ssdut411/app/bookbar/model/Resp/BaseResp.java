package com.ssdut411.app.bookbar.model.Resp;

/**
 * Created by yao_han on 2015/12/23.
 */
public class BaseResp {

    public static int RESULT_SUCCESS = 0;
    public static int RESULT_FAILED = -1;

    private boolean status;
    private String desc;

    public BaseResp() {
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
