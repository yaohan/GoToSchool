package com.ssdut411.app.bookbar.volley.core;

import android.content.Context;

import com.ssdut411.app.bookbar.model.Req.ForgetPasswordReq;
import com.ssdut411.app.bookbar.model.Req.LoginReq;
import com.ssdut411.app.bookbar.model.Req.RegisterReq;
import com.ssdut411.app.bookbar.model.Resp.ForgetPasswordResp;
import com.ssdut411.app.bookbar.model.Resp.LoginResp;
import com.ssdut411.app.bookbar.model.Resp.RegisterResp;
import com.ssdut411.app.bookbar.utils.GsonUtils;
import com.ssdut411.app.bookbar.volley.api.Api;
import com.ssdut411.app.bookbar.volley.api.ApiCallbackListener;
import com.ssdut411.app.bookbar.volley.api.ApiConfig;
import com.ssdut411.app.bookbar.volley.api.ApiImpl;

/**
 * AppAction接口的实现
 * <p/>
 * Created by yao_han on 2015/11/24.
 */
public class AppActionImpl implements AppAction {
    private Context context;
    private Api api;

    public AppActionImpl(Context context) {
        this.context = context;
        this.api = new ApiImpl();
    }

    @Override
    public void login(LoginReq req, final ActionCallbackListener<LoginResp> listener) {
        String url = ApiConfig.BASE_URL + "/login";
        String reqJson = req.toGetFormat();

        api.login(url, reqJson, context, new ApiCallbackListener<LoginResp>() {
            @Override
            public void onSuccess(LoginResp data) {
                listener.onSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    @Override
    public void forgetPassword(ForgetPasswordReq req, final ActionCallbackListener<ForgetPasswordResp> listener) {
        String url = ApiConfig.BASE_URL + "/forgetPassword";
        String reqJson = req.toGetFormat();

        api.forgetPassword(url, reqJson, context, new ApiCallbackListener<ForgetPasswordResp>() {
            @Override
            public void onSuccess(ForgetPasswordResp data) {
                listener.onSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    @Override
    public void register(RegisterReq req, final ActionCallbackListener<RegisterResp> listener) {
        String url = ApiConfig.BASE_URL + "/register";
        String reqJson = req.toGetFormat();

        api.register(url, reqJson, context, new ApiCallbackListener<RegisterResp>() {
            @Override
            public void onSuccess(RegisterResp data) {
                listener.onSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

}



