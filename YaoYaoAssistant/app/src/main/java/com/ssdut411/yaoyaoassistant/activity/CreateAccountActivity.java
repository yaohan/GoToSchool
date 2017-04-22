package com.ssdut411.yaoyaoassistant.activity;

import android.view.View;

import com.ssdut411.yaoyaoassistant.R;
import com.ssdut411.yaoyaoassistant.core.ActionCallbackListener;
import com.ssdut411.yaoyaoassistant.core.AppAction;
import com.ssdut411.yaoyaoassistant.core.AppActionImpl;
import com.ssdut411.yaoyaoassistant.model.Account;
import com.ssdut411.yaoyaoassistant.model.resp.BaseResp;
import com.ssdut411.yaoyaoassistant.utils.L;
import com.ssdut411.yaoyaoassistant.utils.T;

/**
 * Created by yao_han on 2017/4/22.
 */
public class CreateAccountActivity extends BaseActivity {
    @Override
    protected String initTitle() {
        return "创建账户";
    }

    @Override
    protected int initMenu() {
        return 0;
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_create_account;
    }

    @Override
    protected void initViews() {
        getButton(R.id.bt_create_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppAction action = new AppActionImpl(context);
                Account account = new Account();
                account.setName(getEditText(R.id.et_create_account_name).getText().toString());
                account.setMoney(Double.parseDouble(getEditText(R.id.et_create_account_money).getText().toString()));
                account.setDesc(getEditText(R.id.et_create_account_desc).getText().toString());
                action.createAccount(account, new ActionCallbackListener<BaseResp>() {
                    @Override
                    public void onSuccess(BaseResp data) {
                        T.showShort(context,data.getDesc());
                        if(data.getStatus()){
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        L.i(message);
                    }
                });
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
