package com.ssdut411.app.bookbar.activity.person;

import android.view.View;
import android.widget.EditText;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.system.BaseActivity;
import com.ssdut411.app.bookbar.utils.KeyBoardUtils;
import com.ssdut411.app.bookbar.utils.L;
import com.ssdut411.app.bookbar.utils.T;

/**
 * Created by yao_han on 2016/10/30.
 */
public class ChangePasswordActivity extends BaseActivity {
    private EditText etText;
    @Override
    protected String initTitle() {
        return "修改密码";
    }

    @Override
    protected int initMenu() {
        return 0;
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void initViews() {
        etText = getEditText(R.id.et_change_phone_number);
        getEditText(R.id.et_change_phone_number).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    reset();
                    getView(R.id.v_change_phone_div).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        getEditText(R.id.et_change_check).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    reset();
                    getView(R.id.v_change_check_div).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        getEditText(R.id.et_change_password).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    reset();
                    getView(R.id.v_change_password_div).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        getEditText(R.id.et_change_password_again).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    reset();
                    getView(R.id.v_change_password_again_div).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        getButton(R.id.bt_change_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(context,"修改");
            }
        });
    }

    private void reset() {
        getView(R.id.v_change_phone_div).setBackgroundColor(getResources().getColor(R.color.divide_gray));
        getView(R.id.v_change_check_div).setBackgroundColor(getResources().getColor(R.color.divide_gray));
        getView(R.id.v_change_password_div).setBackgroundColor(getResources().getColor(R.color.divide_gray));
        getView(R.id.v_change_password_again_div).setBackgroundColor(getResources().getColor(R.color.divide_gray));
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void showView() {
        KeyBoardUtils.openKeyboard(etText, context);
        mToolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                L.i("etText == null"+(etText == null));
                KeyBoardUtils.closeKeyboard(etText, context);
            }
        });
    }
}
