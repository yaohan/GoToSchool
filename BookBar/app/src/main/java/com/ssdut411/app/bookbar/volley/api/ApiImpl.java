package com.ssdut411.app.bookbar.volley.api;

import com.ssdut411.app.bookbar.model.Resp.ForgetPasswordResp;
import com.ssdut411.app.bookbar.model.Resp.LoginResp;
import com.ssdut411.app.bookbar.model.Resp.RegisterResp;
import com.ssdut411.app.bookbar.volley.VolleyUtil;

/**
 * Api接口实现类
 *
 * Created by yao_han on 2015/11/24.
 */
public class ApiImpl implements Api{

    @Override
    public void login(String url, String reqJson, Object tag, ApiCallbackListener<LoginResp> listener) {
        VolleyUtil.doGet(url+ reqJson, LoginResp.class, tag, listener);
    }

    @Override
    public void forgetPassword(String url, String reqJson, Object tag, ApiCallbackListener<ForgetPasswordResp> listener) {
        VolleyUtil.doGet(url+reqJson, ForgetPasswordResp.class, tag, listener);
    }

    @Override
    public void register(String url, String reqJson, Object tag, ApiCallbackListener<RegisterResp> listener) {
        VolleyUtil.doGet(url+reqJson, RegisterResp.class, tag, listener);
    }
}
