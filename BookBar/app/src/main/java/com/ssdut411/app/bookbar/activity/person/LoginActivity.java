package com.ssdut411.app.bookbar.activity.person;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.system.BaseActivity;
import com.ssdut411.app.bookbar.activity.system.MainApplication;
import com.ssdut411.app.bookbar.model.Req.LoginReq;
import com.ssdut411.app.bookbar.model.Resp.LoginResp;
import com.ssdut411.app.bookbar.utils.KeyBoardUtils;
import com.ssdut411.app.bookbar.utils.L;
import com.ssdut411.app.bookbar.utils.T;
import com.ssdut411.app.bookbar.volley.core.ActionCallbackListener;
import com.ssdut411.app.bookbar.volley.core.AppAction;
import com.ssdut411.app.bookbar.volley.core.AppActionImpl;

/**
 * Created by LENOVO on 2016/10/29.
 */
public class LoginActivity extends BaseActivity {
    private EditText etText;

    @Override
    protected String initTitle() {
        return "登录";
    }

    @Override
    protected int initMenu() {
        return 0;
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {
        etText = getEditText(R.id.et_login_phone_number);
        getTextView(R.id.tv_login_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, RegisterActivity.class));
            }
        });
        getTextView(R.id.tv_login_forget_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ForgetPasswordActivity.class));
            }
        });
        getEditText(R.id.et_login_phone_number).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    reset();
                    getView(R.id.v_login_phone_div).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }

        });
        getEditText(R.id.et_login_password).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    reset();
                    getView(R.id.v_login_password_div).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }

        });
        getButton(R.id.bt_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()) {
                    L.i("start");
                    AppAction action = new AppActionImpl(context);
                    LoginReq loginReq = new LoginReq();
                    loginReq.setPhoneNumber(getEditText(R.id.et_login_phone_number).getText().toString());
                    loginReq.setPassword(getEditText(R.id.et_login_password).getText().toString());
                    action.login(loginReq, new ActionCallbackListener<LoginResp>() {
                        @Override
                        public void onSuccess(LoginResp data) {
                            if (data.isStatus()) {
                                MainApplication.getInstance().setUserId(data.getUserId());
                                MainApplication.getInstance().setPhoneNumber(getEditText(R.id.et_login_phone_number).getText().toString());
                                T.showShort(context, data.getDesc());
                                finish();
                                KeyBoardUtils.closeKeyboard(etText, context);
                            } else {
                                T.showShort(context, data.getDesc());
                            }
                        }

                        @Override
                        public void onFailure(String message) {
                            T.showShort(context, message);
                        }
                    });
                }

            }

        });
    }

    private boolean check() {
        L.i("length：" + getEditText(R.id.et_login_phone_number).getText().toString().length());
        if (getEditText(R.id.et_login_phone_number).getText().length() != 11) {
            T.showShort(context, "手机号格式不正确");
            return false;
        } else if (getEditText(R.id.et_login_password).getText().length() == 0) {
            T.showShort(context, "密码不能为空");
            return false;
        }
        return true;
    }

    private void reset() {
        getView(R.id.v_login_phone_div).setBackgroundColor(getResources().getColor(R.color.divide_gray));
        getView(R.id.v_login_password_div).setBackgroundColor(getResources().getColor(R.color.divide_gray));
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void showView() {
        mToolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                KeyBoardUtils.closeKeyboard(etText, context);
            }
        });
        KeyBoardUtils.openKeyboard(etText, context);
    }
}
