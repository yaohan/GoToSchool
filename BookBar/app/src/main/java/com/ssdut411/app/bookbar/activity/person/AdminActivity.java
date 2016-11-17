package com.ssdut411.app.bookbar.activity.person;

import android.content.Intent;
import android.view.View;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.mainPage.CaptureActivity;
import com.ssdut411.app.bookbar.activity.show.CreateDBActivity;
import com.ssdut411.app.bookbar.activity.system.BaseActivity;
import com.ssdut411.app.bookbar.activity.system.MainApplication;

/**
 * Created by LENOVO on 2016/11/7.
 */
public class AdminActivity extends BaseActivity {
    @Override
    protected String initTitle() {
        return "管理员模式";
    }

    @Override
    protected int initMenu() {
        return 0;
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_admin;
    }

    @Override
    protected void initViews() {
        getLinearLayout(R.id.ll_admin_train).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CreateDBActivity.class));
            }
        });
        getLinearLayout(R.id.ll_admin_add_book).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainApplication.getInstance().setIsAdmin(true);
                startActivity(new Intent(context, CaptureActivity.class));
            }
        });
        getLinearLayout(R.id.ll_admin_server).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, SetServerActivity.class));
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void showView() {
        setCanBack();
    }
}
