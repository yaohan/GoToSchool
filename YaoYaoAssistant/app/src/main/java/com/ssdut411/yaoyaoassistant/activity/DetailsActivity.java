package com.ssdut411.yaoyaoassistant.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.ssdut411.yaoyaoassistant.R;
import com.ssdut411.yaoyaoassistant.model.Details;
import com.ssdut411.yaoyaoassistant.widget.CommonAdapter;
import com.ssdut411.yaoyaoassistant.widget.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yao_han on 2017/4/22.
 */
public class DetailsActivity extends BaseActivity {
    private List<Details> list;

    @Override
    protected String initTitle() {
        return "明细";
    }

    @Override
    protected int initMenu() {
        return 0;
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initViews() {
        getListView(R.id.lv_detail).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context,CreateActivity.class);
                intent.putExtra("title","修改");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void loadData() {
        list = new ArrayList<>();
        list.add(new Details("宫保鸡丁",-10,"2017-4-22 12:10",12432.3));
        list.add(new Details("宫保鸡丁",-10,"2017-4-22 12:10",12432.3));
        list.add(new Details("宫保鸡丁",-10,"2017-4-22 12:10",12432.3));
        list.add(new Details("宫保鸡丁",-10,"2017-4-22 12:10",12432.3));
    }

    @Override
    protected void showView() {
        setCanBack();
        getListView(R.id.lv_detail).setAdapter(new CommonAdapter<Details>(context,list,R.layout.item_detail) {
            @Override
            public void convert(ViewHolder viewHolder, Details details, int position) {
                viewHolder.getTextView(R.id.tv_detail_name).setText(details.getName());
                viewHolder.getTextView(R.id.tv_detail_money).setText(details.getMoney()+"");
                viewHolder.getTextView(R.id.tv_detail_time).setText(details.getDate());
                viewHolder.getTextView(R.id.tv_detail_sum).setText(details.getSum()+"");
            }
        });
    }
}
