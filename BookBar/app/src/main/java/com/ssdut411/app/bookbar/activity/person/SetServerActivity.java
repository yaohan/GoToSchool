package com.ssdut411.app.bookbar.activity.person;

import android.content.Intent;
import android.view.View;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.system.BaseActivity;
import com.ssdut411.app.bookbar.activity.system.MainActivity;
import com.ssdut411.app.bookbar.activity.system.MainApplication;
import com.ssdut411.app.bookbar.utils.ActivityStackUtils;
import com.ssdut411.app.bookbar.utils.KeyBoardUtils;
import com.ssdut411.app.bookbar.utils.SPUtils;

/**
 * Created by LENOVO on 2016/11/7.
 */
public class SetServerActivity extends BaseActivity {
    @Override
    protected String initTitle() {
        return "设置服务器";
    }

    @Override
    protected int initMenu() {
        return 0;
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_set_server;
    }

    @Override
    protected void initViews() {
        getButton(R.id.bt_server_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String server = getEditText(R.id.et_server_server).getText().toString();
                MainApplication.getInstance().setServer(server);
                SPUtils.put(context, "server", server);
                KeyBoardUtils.closeKeyboard(getEditText(R.id.et_server_server), context);
                startActivity(new Intent(context, MainActivity.class));
                ActivityStackUtils.getInstance().exit();
            }
        });
    }

    @Override
    protected void loadData() {
        String server = SPUtils.get(context,"server","").toString();
        if(server.length() != 0){
            getEditText(R.id.et_server_server).setText(server);
        }
    }

    @Override
    protected void showView() {mToolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                KeyBoardUtils.closeKeyboard(getEditText(R.id.et_server_server), context);
            }
        });
        KeyBoardUtils.openKeyboard(getEditText(R.id.et_server_server), context);
    }
}
