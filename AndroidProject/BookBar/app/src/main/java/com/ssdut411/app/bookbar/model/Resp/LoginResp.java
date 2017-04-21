package com.ssdut411.app.bookbar.model.Resp;


/**
 * Created by yao_han on 2015/12/23.
 */
public class LoginResp extends BaseResp {


    public static String DESC_SUCCESS = "登录成功";
    public static String DESC_ERROR = "手机号或密码错误";
    private String userId;

    public LoginResp() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
