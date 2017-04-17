package com.ssdut411.app.bookbar.model.Req;

/**
 * Created by LENOVO on 2016/11/5.
 */
public class GetCollectionReq extends BaseReq {
    private String userId;

    public GetCollectionReq() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
