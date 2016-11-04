package com.ssdut411.app.bookbar.volley.api;


import com.ssdut411.app.bookbar.model.Resp.ForgetPasswordResp;
import com.ssdut411.app.bookbar.model.Resp.LoginResp;
import com.ssdut411.app.bookbar.model.Resp.RegisterResp;

/**
 * Api接口
 * <p/>
 * Created by yao_han on 2015/11/24.
 */
public interface Api {
    public void login(String url, String reqJson, Object tag, ApiCallbackListener<LoginResp> listener);
    public void forgetPassword(String url, String reqJson, Object tag, ApiCallbackListener<ForgetPasswordResp> listener);
    public void register(String url, String reqJson, Object tag, ApiCallbackListener<RegisterResp> listener);
}
