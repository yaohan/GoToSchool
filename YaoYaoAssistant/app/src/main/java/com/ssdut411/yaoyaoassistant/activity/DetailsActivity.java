package com.ssdut411.yaoyaoassistant.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.ssdut411.yaoyaoassistant.R;
import com.ssdut411.yaoyaoassistant.core.ActionCallbackListener;
import com.ssdut411.yaoyaoassistant.core.AppAction;
import com.ssdut411.yaoyaoassistant.core.AppActionImpl;
import com.ssdut411.yaoyaoassistant.model.Details;
import com.ssdut411.yaoyaoassistant.model.req.DetailReq;
import com.ssdut411.yaoyaoassistant.model.resp.DetailResp;
import com.ssdut411.yaoyaoassistant.utils.L;
import com.ssdut411.yaoyaoassistant.utils.T;
import com.ssdut411.yaoyaoassistant.widget.CommonAdapter;
import com.ssdut411.yaoyaoassistant.widget.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
                Intent intent = new Intent(context,CreateDetailsActivity.class);
                intent.putExtra("title","修改");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void loadData() {
        int accountId = getIntent().getIntExtra("accountId",-1);
        AppAction action = new AppActionImpl(context);
        DetailReq detailReq = new DetailReq();
        if(accountId == -1){
            detailReq.setAccountId("accountId");
        }else{
            detailReq.setAccountId(accountId+"");
        }
        action.getDetails(detailReq, new ActionCallbackListener<DetailResp>() {
            @Override
            public void onSuccess(DetailResp data) {
                if (data.getStatus()) {
                    list = data.getData();
                    setList();
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private void setList() {
        getListView(R.id.lv_detail).setAdapter(new CommonAdapter<Details>(context,list,R.layout.item_detail) {
            @Override
            public void convert(ViewHolder viewHolder, Details details, int position) {
                L.i("time:"+details.getTime());
                viewHolder.getTextView(R.id.tv_detail_name).setText(details.getName());
                double money = details.getMoney();
                if(details.getType() == 0){
                    money = -money;
                }
                viewHolder.getTextView(R.id.tv_detail_money).setText(money+"");
                String time = details.getTime();
                if(time != null){
                    time = time.replace("T"," ");
                    time = time.replace(":00.000Z","");
                }else{
                    time = "";
                }
                viewHolder.getTextView(R.id.tv_detail_time).setText(time);
                viewHolder.getTextView(R.id.tv_detail_sum).setText(details.getSum()+"");
            }
        });
    }

    @Override
    protected void showView() {
        setCanBack();
    }
}
