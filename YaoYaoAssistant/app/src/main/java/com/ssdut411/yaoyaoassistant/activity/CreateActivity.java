package com.ssdut411.yaoyaoassistant.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.ssdut411.yaoyaoassistant.R;
import com.ssdut411.yaoyaoassistant.core.AppAction;
import com.ssdut411.yaoyaoassistant.core.AppActionImpl;
import com.ssdut411.yaoyaoassistant.model.Details;

import java.util.Calendar;

/**
 * Created by yao_han on 2017/4/22.
 */
public class CreateActivity extends BaseActivity {
    private String date;
    private String time;
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
        Calendar c = Calendar.getInstance();//
        final int mYear = c.get(Calendar.YEAR); // 获取当前年份
        final int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        final int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        date = mYear+"-"+mMonth+"-"+mDay+" ";
        final int mHour = c.get(Calendar.HOUR_OF_DAY);//时
        final int mMinute = c.get(Calendar.MINUTE);//分
        time = mHour+":"+mMinute+":00";
        getTextView(R.id.tv_create_date).setText(mMonth+"-"+mDay);
        getTextView(R.id.tv_create_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        getTextView(R.id.tv_create_date).setText((monthOfYear + 1) + "-" + dayOfMonth);
                        date = year+"-"+(monthOfYear+1)+"-"+dayOfMonth+" ";
                    }
                }, mYear, mMonth, mDay).show();
            }
        });
        getTextView(R.id.tv_create_time).setText(mHour + ":" + mMinute);
        getTextView(R.id.tv_create_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        getTextView(R.id.tv_create_time).setText(hourOfDay + ":" + minute);
                        time = hourOfDay+":"+minute+":00";
                    }
                }, mHour, mMinute, true).show();
            }
        });
        getTextView(R.id.bt_create_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Details details = new Details();
                details.setName(getEditText(R.id.et_create_name).getText().toString());
                details.setAccountId(1);
                details.setDate(date + time);
                details.setMoney(Double.parseDouble(getEditText(R.id.et_create_money).getText().toString()));
                details.setTagId(1);
                if(getIntent().getStringExtra("title").equals("支出")){
                    details.setType(0);
                }else{
                    details.setType(1);
                }
                AppAction action = new AppActionImpl(context);
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
