package com.ssdut411.yaoyaoassistant.core;

import android.content.Context;

import com.ssdut411.yaoyaoassistant.api.Api;
import com.ssdut411.yaoyaoassistant.api.ApiCallbackListener;
import com.ssdut411.yaoyaoassistant.api.ApiConfig;
import com.ssdut411.yaoyaoassistant.api.ApiImpl;
import com.ssdut411.yaoyaoassistant.model.Account;
import com.ssdut411.yaoyaoassistant.model.MainInfo;
import com.ssdut411.yaoyaoassistant.model.req.BaseReq;
import com.ssdut411.yaoyaoassistant.model.resp.BaseResp;
import com.ssdut411.yaoyaoassistant.model.resp.MainInfoResp;
import com.ssdut411.yaoyaoassistant.utils.GsonUtils;
import com.ssdut411.yaoyaoassistant.utils.L;

/**
 * Created by yao_han on 2017/4/22.
 */
public class AppActionImpl implements AppAction {
    private Context context;
    private Api api;

    public AppActionImpl(Context context) {
        this.context = context;
        this.api = new ApiImpl();
    }

    @Override
    public void createAccount(Account req, final ActionCallbackListener<BaseResp> listener) {
        String url = ApiConfig.BASE_URL + "/createAccount";
        String reqJson = GsonUtils.gsonToJsonString(req);
        L.i("req:"+reqJson);
        api.createAccount(url, reqJson, context, new ApiCallbackListener<BaseResp>() {
            @Override
            public void onSuccess(BaseResp data) {
                listener.onSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    @Override
    public void getMainInfo(BaseReq req, final ActionCallbackListener<MainInfoResp> listener) {
        String url = ApiConfig.BASE_URL + "/getMainInfo";
        String reqJson = GsonUtils.gsonToJsonString(req);
        api.getMainInfo(url, reqJson, context, new ApiCallbackListener<MainInfoResp>() {
            @Override
            public void onSuccess(MainInfoResp data) {
                listener.onSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });

    }
}
