package com.ssdut411.yaoyaoassistant.core;

import android.support.annotation.BoolRes;

import com.ssdut411.yaoyaoassistant.model.Account;
import com.ssdut411.yaoyaoassistant.model.Details;
import com.ssdut411.yaoyaoassistant.model.MainInfo;
import com.ssdut411.yaoyaoassistant.model.Transfer;
import com.ssdut411.yaoyaoassistant.model.req.BaseReq;
import com.ssdut411.yaoyaoassistant.model.req.DetailReq;
import com.ssdut411.yaoyaoassistant.model.resp.BaseResp;
import com.ssdut411.yaoyaoassistant.model.resp.DetailResp;
import com.ssdut411.yaoyaoassistant.model.resp.ListResp;
import com.ssdut411.yaoyaoassistant.model.resp.MainInfoResp;

/**
 * Created by yao_han on 2017/4/22.
 */
public interface AppAction {
    public void createAccount(Account account, ActionCallbackListener<BaseResp> listener);
    public void getMainInfo(BaseReq req, ActionCallbackListener<MainInfoResp> listener);
    public void createDetails(Details details, ActionCallbackListener<BaseResp> listener);
    public void getList(BaseReq req,ActionCallbackListener<ListResp> listener);
    public void createTransfer(Transfer transfer,ActionCallbackListener<BaseResp> listener);
    public void getDetails(DetailReq req, ActionCallbackListener<DetailResp> listener);
}
