package com.ssdut411.app.bookbarstatic.activity.person;

import android.view.View;
import android.widget.EditText;

import com.ssdut411.app.bookbarstatic.R;
import com.ssdut411.app.bookbarstatic.activity.system.BaseActivity;
import com.ssdut411.app.bookbarstatic.utils.T;

/**
 * Created by LENOVO on 2016/10/29.
 */
public class ForgetPasswordActivity extends BaseActivity {
    private EditText phoneNumber,password,passwordAgain;
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
        phoneNumber = getEditText(R.id.et_forget_phone_number);
        password = getEditText(R.id.et_forget_password);
        passwordAgain = getEditText(R.id.et_forget_password_again);

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
        getButton(R.id.bt_forget_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check()){

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
