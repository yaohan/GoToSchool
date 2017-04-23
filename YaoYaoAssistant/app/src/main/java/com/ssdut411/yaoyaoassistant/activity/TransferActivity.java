package com.ssdut411.yaoyaoassistant.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import com.google.gson.Gson;
import com.ssdut411.yaoyaoassistant.R;
import com.ssdut411.yaoyaoassistant.core.ActionCallbackListener;
import com.ssdut411.yaoyaoassistant.core.AppAction;
import com.ssdut411.yaoyaoassistant.core.AppActionImpl;
import com.ssdut411.yaoyaoassistant.model.Account;
import com.ssdut411.yaoyaoassistant.model.Transfer;
import com.ssdut411.yaoyaoassistant.model.req.BaseReq;
import com.ssdut411.yaoyaoassistant.model.resp.BaseResp;
import com.ssdut411.yaoyaoassistant.model.resp.ListResp;
import com.ssdut411.yaoyaoassistant.utils.GsonUtils;
import com.ssdut411.yaoyaoassistant.utils.L;
import com.ssdut411.yaoyaoassistant.utils.T;
import com.ssdut411.yaoyaoassistant.widget.CommonAdapter;
import com.ssdut411.yaoyaoassistant.widget.ViewHolder;

import java.util.List;

/**
 * Created by yao_han on 2017/4/22.
 */
public class TransferActivity extends CreateDetailsActivity {
    private int sourceAccountId;
    private int targetAccountId;
    @Override
    protected String initTitle() {
        return "转账";
    }

    @Override
    protected int initMenu() {
        return 0;
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_transfer;
    }

    @Override
    protected void initViews() {
        setTime(R.id.tv_transfer_date, R.id.tv_transfer_time);
        getTextView(R.id.tv_transfer_source).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context).setTitle("请选择账户").setAdapter(new CommonAdapter<Account>(context, accountList, R.layout.item_center) {
                    @Override
                    public void convert(ViewHolder viewHolder, Account account, int position) {
                        viewHolder.getTextView(R.id.tv_center).setText(account.getName());
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sourceAccountId = accountList.get(which).getId();
                        getTextView(R.id.tv_transfer_source).setText(accountList.get(which).getName());
                    }
                }).show();
            }
        });
        getTextView(R.id.tv_transfer_target).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context).setTitle("请选择账户").setAdapter(new CommonAdapter<Account>(context, accountList, R.layout.item_center) {
                    @Override
                    public void convert(ViewHolder viewHolder, Account account, int position) {
                        viewHolder.getTextView(R.id.tv_center).setText(account.getName());
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        targetAccountId = accountList.get(which).getId();
                        getTextView(R.id.tv_transfer_target).setText(accountList.get(which).getName());
                    }
                }).show();
            }
        });
        getButton(R.id.bt_transfer_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transfer transfer = new Transfer();
                transfer.setTime(date + time);

                transfer.setMoney(Double.parseDouble(getEditText(R.id.et_transfer_money).getText().toString()));
                L.i("desc:" + getEditText(R.id.et_transfer_name).getText().toString());
                transfer.setDesc(getEditText(R.id.et_transfer_name).getText().toString());
                transfer.setSource(sourceAccountId);
                transfer.setTarget(targetAccountId);
                L.i("transfer:" + GsonUtils.gsonToJsonString(transfer));
                AppAction action = new AppActionImpl(context);
                action.createTransfer(transfer, new ActionCallbackListener<BaseResp>() {
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
        super.loadData();
    }

    @Override
    protected void showView() {
        setCanBack();
    }

    @Override
    protected void setDefault() {
        L.i("default:" + GsonUtils.gsonToJsonString(accountList));
        getTextView(R.id.tv_transfer_source).setText(accountList.get(0).getName());
        sourceAccountId = accountList.get(0).getId();
        getTextView(R.id.tv_transfer_target).setText(accountList.get(0).getName());
        targetAccountId = accountList.get(0).getId();

    }
}
