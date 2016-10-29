package com.ssdut411.app.bookbar.activity.person;

import android.view.View;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.system.BaseActivity;

/**
 * Created by LENOVO on 2016/10/29.
 */
public class ForgetPasswordActivity extends BaseActivity {
    @Override
    protected String initTitle() {
        return "忘记密码";
    }

    @Override
    protected int initMenu() {
        return 0;
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void initViews() {

        getEditText(R.id.et_forget_phone_number).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    reset();
                    getView(R.id.v_forget_phone_div).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        getEditText(R.id.et_forget_check).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    reset();
                    getView(R.id.v_forget_check_div).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        getEditText(R.id.et_forget_password).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    reset();
                    getView(R.id.v_forget_password_div).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        getEditText(R.id.et_forget_password_again).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    reset();
                    getView(R.id.v_forget_password_again_div).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
    }

    private void reset() {
        getView(R.id.v_forget_phone_div).setBackgroundColor(getResources().getColor(R.color.divide_gray));
        getView(R.id.v_forget_check_div).setBackgroundColor(getResources().getColor(R.color.divide_gray));
        getView(R.id.v_forget_password_div).setBackgroundColor(getResources().getColor(R.color.divide_gray));
        getView(R.id.v_forget_password_again_div).setBackgroundColor(getResources().getColor(R.color.divide_gray));
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void showView() {
        setCanBack();
    }
}
