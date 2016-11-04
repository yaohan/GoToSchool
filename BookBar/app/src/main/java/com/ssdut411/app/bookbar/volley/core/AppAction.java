package com.ssdut411.app.bookbar.volley.core;

import com.ssdut411.app.bookbar.model.Req.ForgetPasswordReq;
import com.ssdut411.app.bookbar.model.Req.LoginReq;
import com.ssdut411.app.bookbar.model.Req.RegisterReq;
import com.ssdut411.app.bookbar.model.Resp.ForgetPasswordResp;
import com.ssdut411.app.bookbar.model.Resp.LoginResp;
import com.ssdut411.app.bookbar.model.Resp.RegisterResp;

/**
 * 接收App层的各种Action
 *
 * Created by yao_han on 2015/11/24.
 */
public interface AppAction {
    public void login(LoginReq req, ActionCallbackListener<LoginResp> listener);
    public void forgetPassword(ForgetPasswordReq req, ActionCallbackListener<ForgetPasswordResp> listener);
    public void register(RegisterReq req, ActionCallbackListener<RegisterResp> listener);
}
