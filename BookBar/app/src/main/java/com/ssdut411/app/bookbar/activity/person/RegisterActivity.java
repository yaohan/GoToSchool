package com.ssdut411.app.bookbar.activity.person;

import android.view.View;
import android.widget.EditText;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.system.BaseActivity;
import com.ssdut411.app.bookbar.model.Req.RegisterReq;
import com.ssdut411.app.bookbar.model.Resp.RegisterResp;
import com.ssdut411.app.bookbar.utils.T;
import com.ssdut411.app.bookbar.volley.core.ActionCallbackListener;
import com.ssdut411.app.bookbar.volley.core.AppAction;
import com.ssdut411.app.bookbar.volley.core.AppActionImpl;

/**
 * Created by LENOVO on 2016/10/29.
 */
public class RegisterActivity extends BaseActivity {
    private EditText phoneNumber,password,passwordAgain;
    @Override
    protected String initTitle() {
        return "注册";
    }

    @Override
    protected int initMenu() {
        return 0;
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_register;
    }

    @Override
    protected void initViews() {
        phoneNumber = getEditText(R.id.et_register_phone_number);
        password = getEditText(R.id.et_register_password);
        passwordAgain = getEditText(R.id.et_register_password_again);
        phoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    reset();
                    getView(R.id.v_register_phone_div).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }

        });
        getEditText(R.id.et_register_check).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    reset();
                    getView(R.id.v_register_check_div).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }

        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    reset();
                    getView(R.id.v_register_password_div).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }

        });
        passwordAgain.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    reset();
                    getView(R.id.v_register_password_again_div).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }

        });
        getButton(R.id.bt_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check()){
                    AppAction action = new AppActionImpl(context);
                    RegisterReq registerReq = new RegisterReq();
                    registerReq.setPhoneNumber(phoneNumber.getText().toString());
                    registerReq.setPassword(password.getText().toString());
                    action.register(registerReq, new ActionCallbackListener<RegisterResp>() {
                        @Override
                        public void onSuccess(RegisterResp data) {
                            if(data.isStatus()){
                                T.showShort(context,data.getDesc());
                                finish();
                            }else{
                                T.showShort(context,data.getDesc());
                            }
                        }

                        @Override
                        public void onFailure(String message) {

                            T.showShort(context,getString(R.string.error_message));
                        }
                    });
                }
            }
        });
    }

    private boolean check() {
        if(phoneNumber.getText().toString().length() != 11){
            T.showShort(context,"手机号格式不正确");
            return false;
        }else if(password.getText().toString().length() == 0){
            T.showShort(context,"密码不能为空");
            return false;
        }else if(!password.getText().toString().equals(passwordAgain.getText().toString())){
            T.showShort(context,"两次输入密码不同");
            return false;
        }
        return true;
    }
    private void reset() {
        getView(R.id.v_register_phone_div).setBackgroundColor(getResources().getColor(R.color.divide_gray));
        getView(R.id.v_register_check_div).setBackgroundColor(getResources().getColor(R.color.divide_gray));
        getView(R.id.v_register_password_div).setBackgroundColor(getResources().getColor(R.color.divide_gray));
        getView(R.id.v_register_password_again_div).setBackgroundColor(getResources().getColor(R.color.divide_gray));
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void showView() {
        setCanBack();
    }
}
