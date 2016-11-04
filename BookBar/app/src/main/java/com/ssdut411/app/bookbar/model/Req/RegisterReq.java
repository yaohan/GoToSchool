package com.ssdut411.app.bookbar.model.Req;

/**
 * Created by yao_han on 2015/12/23.
 */
public class RegisterReq extends BaseReq{
    private String phoneNumber;
    private String password;

    public RegisterReq() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
