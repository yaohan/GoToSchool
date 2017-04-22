package com.ssdut411.yaoyaoassistant.core;

import com.ssdut411.yaoyaoassistant.model.Account;
import com.ssdut411.yaoyaoassistant.model.MainInfo;
import com.ssdut411.yaoyaoassistant.model.req.BaseReq;
import com.ssdut411.yaoyaoassistant.model.resp.BaseResp;
import com.ssdut411.yaoyaoassistant.model.resp.MainInfoResp;

/**
 * Created by yao_han on 2017/4/22.
 */
public interface AppAction {
    public void createAccount(Account account, ActionCallbackListener<BaseResp> listener);
    public void getMainInfo(BaseReq req, ActionCallbackListener<MainInfoResp> listener);
}
