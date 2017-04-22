package com.ssdut411.yaoyaoassistant.api;

import com.ssdut411.yaoyaoassistant.model.resp.BaseResp;
import com.ssdut411.yaoyaoassistant.model.MainInfo;
import com.ssdut411.yaoyaoassistant.model.resp.MainInfoResp;

/**
 * Created by yao_han on 2017/4/22.
 */
public interface Api {
    public void getMainInfo(String url, String reqJson, Object tag, ApiCallbackListener<MainInfoResp> listener);
    public void createAccount(String url, String reqJson, Object tag, ApiCallbackListener<BaseResp> listener);
}
