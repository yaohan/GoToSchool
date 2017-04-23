package com.ssdut411.yaoyaoassistant.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.ssdut411.yaoyaoassistant.R;
import com.ssdut411.yaoyaoassistant.core.ActionCallbackListener;
import com.ssdut411.yaoyaoassistant.core.AppAction;
import com.ssdut411.yaoyaoassistant.core.AppActionImpl;
import com.ssdut411.yaoyaoassistant.model.Account;
import com.ssdut411.yaoyaoassistant.model.Details;
import com.ssdut411.yaoyaoassistant.model.Tag;
import com.ssdut411.yaoyaoassistant.model.req.BaseReq;
import com.ssdut411.yaoyaoassistant.model.resp.BaseResp;
import com.ssdut411.yaoyaoassistant.model.resp.ListResp;
import com.ssdut411.yaoyaoassistant.model.resp.MainInfoResp;
import com.ssdut411.yaoyaoassistant.utils.GsonUtils;
import com.ssdut411.yaoyaoassistant.utils.L;
import com.ssdut411.yaoyaoassistant.utils.T;
import com.ssdut411.yaoyaoassistant.widget.CommonAdapter;
import com.ssdut411.yaoyaoassistant.widget.ViewHolder;

import java.util.Calendar;
import java.util.List;

/**
 * Created by yao_han on 2017/4/22.
 */
public class CreateDetailsActivity extends BaseActivity {
    protected String date;
    protected String time;
    protected List<Account> accountList;
    private List<Tag> tagList;
    private int accountId;
    private int tagId;
    @Override
    protected String initTitle() {
        return getIntent().getStringExtra("title");
    }

    @Override
    protected int initMenu() {
        return 0;
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_create_detail;
    }

    @Override
    protected void initViews() {
        setTime(R.id.tv_create_date,R.id.tv_create_time);
        getTextView(R.id.bt_create_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Details details = new Details();
                details.setName(getEditText(R.id.et_create_name).getText().toString());
                details.setAccountId(accountId);
                details.setTime(date + time);
                details.setMoney(Double.parseDouble(getEditText(R.id.et_create_money).getText().toString()));
                details.setTagId(tagId);
                if(getIntent().getStringExtra("title").equals("支出")){
                    details.setType(0);
                }else{
                    details.setType(1);
                }
                L.i("Details："+GsonUtils.gsonToJsonString(details));
                AppAction action = new AppActionImpl(context);
                action.createDetails(details, new ActionCallbackListener<BaseResp>() {
                    @Override
                    public void onSuccess(BaseResp data) {
                        L.i(GsonUtils.gsonToJsonString(data));
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
        getTextView(R.id.tv_create_account).setOnClickListener(new View.OnClickListener() {
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
                        accountId = accountList.get(which).getId();
                        getTextView(R.id.tv_create_account).setText(accountList.get(which).getName());
                    }
                }).show();
            }
        });
        getTextView(R.id.tv_create_tag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context).setTitle("请选择类型").setAdapter(new CommonAdapter<Tag>(context, tagList, R.layout.item_center) {
                    @Override
                    public void convert(ViewHolder viewHolder, Tag tag, int position) {
                        viewHolder.getTextView(R.id.tv_center).setText(tag.getName());
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tagId = tagList.get(which).getId();
                        getTextView(R.id.tv_create_tag).setText(tagList.get(which).getName());
                    }
                }).show();
            }
        });
    }

    protected void setTime(final int dateId, final int timeId) {
        final Calendar c = Calendar.getInstance();//
        final int mYear = c.get(Calendar.YEAR); // 获取当前年份
        final int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        final int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        date = mYear+"-"+mMonth+"-"+mDay+" ";
        final int mHour = c.get(Calendar.HOUR_OF_DAY);//时
        final int mMinute = c.get(Calendar.MINUTE);//分
        time = mHour+":"+mMinute+":00";
        getTextView(dateId).setText(mMonth+"-"+mDay);
        getTextView(dateId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        getTextView(dateId).setText(String.format("%02d",(monthOfYear + 1)) + "-" + String.format("%02d",dayOfMonth));
                        date = year + "-" + String.format("%02d",(monthOfYear + 1)) + "-" + String.format("%02d",dayOfMonth) + " ";
                    }
                }, mYear, mMonth-1, mDay).show();
            }
        });
        getTextView(timeId).setText(mHour + ":" + mMinute);
        getTextView(timeId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        getTextView(timeId).setText(String.format("%02d",hourOfDay) + ":" + String.format("%02d",minute));
                        time = String.format("%02d",hourOfDay) + ":" + String.format("%02d",hourOfDay) + ":00";
                    }
                }, mHour, mMinute, true).show();
            }
        });
    }

    @Override
    protected void loadData() {
        AppAction action = new AppActionImpl(context);
        action.getList(new BaseReq(), new ActionCallbackListener<ListResp>() {
            @Override
            public void onSuccess(ListResp data) {
                accountList = data.getData().getAccount();
                tagList = data.getData().getTag();
                setDefault();
            }

            @Override
            public void onFailure(String message) {
                L.i(message);
            }
        });
    }

    protected void setDefault() {
        getTextView(R.id.tv_create_account).setText(accountList.get(0).getName());
        accountId=accountList.get(0).getId();
        getTextView(R.id.tv_create_tag).setText(tagList.get(0).getName());
        tagId=tagList.get(0).getId();
    }

    @Override
    protected void showView() {
        setCanBack();
    }
}
