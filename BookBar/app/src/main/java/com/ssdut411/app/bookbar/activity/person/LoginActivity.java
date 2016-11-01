package com.ssdut411.app.bookbar.activity.person;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.system.BaseActivity;
import com.ssdut411.app.bookbar.utils.KeyBoardUtils;
import com.ssdut411.app.bookbar.utils.T;

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
                startActivity(new Intent(context,RegisterActivity.class));
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
                if(hasFocus){
                    reset();
                    getView(R.id.v_login_password_div).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }

        });
        getButton(R.id.bt_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(context,"登录");
            }
        });
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
                KeyBoardUtils.closeKeyboard(etText,context);
            }
        });
        KeyBoardUtils.openKeyboard(etText,context);
    }
}
