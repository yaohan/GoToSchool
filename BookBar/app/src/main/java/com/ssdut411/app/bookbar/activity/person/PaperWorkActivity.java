package com.ssdut411.app.bookbar.activity.person;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.system.BaseActivity;
import com.ssdut411.app.bookbar.utils.L;
import com.ssdut411.app.bookbar.widget.CommonAdapter;
import com.ssdut411.app.bookbar.widget.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LENOVO on 2016/11/1.
 */
public class PaperWorkActivity extends BaseActivity {
    @Override
    protected String initTitle() {
        return "我的证件";
    }

    @Override
    protected int initMenu() {
        return R.menu.menu_new;
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_paper_work;
    }

    @Override
    protected void initViews() {
        List<String> paperlist = new ArrayList<>();
        paperlist.add("");
        getListView(R.id.lv_paper_list).setAdapter(new CommonAdapter<String>(context, paperlist, R.layout.item_paper_work) {
            @Override
            public void convert(ViewHolder viewHolder, String s, int position) {
                viewHolder.getTextView(R.id.tv_paper_library).setText("大连理工大学开发区图书馆");
                viewHolder.getTextView(R.id.tv_paper_number).setText("201292425");
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                L.i("id:  new:"+item.getItemId()+"  "+R.menu.menu_new);
                switch (item.getItemId()){
                    case R.id.actions_new:
                        startActivity(new Intent(context,NewPaperActivity.class));
                        return true;
                }
                return false;
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
