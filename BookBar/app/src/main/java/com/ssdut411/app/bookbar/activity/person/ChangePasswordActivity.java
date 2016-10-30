package com.ssdut411.app.bookbar.activity.person;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.system.BaseActivity;

/**
 * Created by yao_han on 2016/10/30.
 */
public class ChangePasswordActivity extends BaseActivity {
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

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void showView() {
        setCanBack();
    }
}
