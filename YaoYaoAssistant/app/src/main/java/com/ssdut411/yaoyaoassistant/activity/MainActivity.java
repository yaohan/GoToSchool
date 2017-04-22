package com.ssdut411.yaoyaoassistant.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.ssdut411.yaoyaoassistant.R;
import com.ssdut411.yaoyaoassistant.core.ActionCallbackListener;
import com.ssdut411.yaoyaoassistant.core.AppAction;
import com.ssdut411.yaoyaoassistant.core.AppActionImpl;
import com.ssdut411.yaoyaoassistant.model.Account;
import com.ssdut411.yaoyaoassistant.model.MainInfo;
import com.ssdut411.yaoyaoassistant.model.req.BaseReq;
import com.ssdut411.yaoyaoassistant.model.resp.MainInfoResp;
import com.ssdut411.yaoyaoassistant.utils.GsonUtils;
import com.ssdut411.yaoyaoassistant.utils.L;
import com.ssdut411.yaoyaoassistant.widget.CommonAdapter;
import com.ssdut411.yaoyaoassistant.widget.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    List<Account> list;
    @Override
    protected String initTitle() {
        return "姚姚管家";
    }

    @Override
    protected int initMenu() {
        return 0;
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        getTextView(R.id.tv_main_sum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                startActivity(intent);
            }
        });
        getTextView(R.id.tv_create_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreateAccountActivity.class);
                startActivity(intent);
            }
        });
        getButton(R.id.bt_main_income).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,CreateActivity.class);
                intent.putExtra("title","收入");
                startActivity(intent);
            }
        });
        getButton(R.id.bt_main_outlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreateActivity.class);
                intent.putExtra("title", "支出");
                startActivity(intent);
            }
        });
        getButton(R.id.bt_main_transfer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreateActivity.class);
                intent.putExtra("title", "转账");
                startActivity(intent);
            }
        });
        getListView(R.id.lv_main_account).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, DetailsActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void loadData() {

    }

    private void setList() {
        getListView(R.id.lv_main_account).setAdapter(new CommonAdapter<Account>(context, list, R.layout.item_account) {
            @Override
            public void convert(ViewHolder viewHolder, Account account, int position) {
                viewHolder.getTextView(R.id.tv_account_name).setText(account.getName());
                viewHolder.getTextView(R.id.tv_account_money).setText(account.getMoney() + "");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

    private void updateList() {
        AppAction action = new AppActionImpl(context);
        action.getMainInfo(new BaseReq(), new ActionCallbackListener<MainInfoResp>() {
            @Override
            public void onSuccess(MainInfoResp data) {
                L.i("data:" + GsonUtils.gsonToJsonString(data));
                if (data.getStatus()) {
                    MainInfo mainInfo = data.getData();
                    setSum(mainInfo.getMoney());
                    list = mainInfo.getList();
                    setList();
                } else {
                    L.i(data.getDesc());
                }

            }

            @Override
            public void onFailure(String message) {
                L.i(message);
            }
        });
    }

    @Override
    protected void showView() {

    }

    public void setSum(double sum) {
        getTextView(R.id.tv_main_sum).setText("总金额"+sum+"元");
    }
}
