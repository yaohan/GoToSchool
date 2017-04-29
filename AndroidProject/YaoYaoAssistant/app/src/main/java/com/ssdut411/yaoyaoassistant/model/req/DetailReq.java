package com.ssdut411.yaoyaoassistant.model.req;

/**
 * Created by yao_han on 2017/4/23.
 */
public class DetailReq extends BaseReq {
    private String accountId;

    public DetailReq() {
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
