package com.ssdut411.app.bookbar.activity.person;

import android.view.View;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.system.BaseActivity;

/**
 * Created by LENOVO on 2016/10/29.
 */
public class RegisterActivity extends BaseActivity {
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
        getEditText(R.id.et_register_phone_number).setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        getEditText(R.id.et_register_password).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    reset();
                    getView(R.id.v_register_password_div).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }

        });
        getEditText(R.id.et_register_password_again).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    reset();
                    getView(R.id.v_register_password_again_div).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }

        });
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
