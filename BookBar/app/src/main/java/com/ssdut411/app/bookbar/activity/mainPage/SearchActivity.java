package com.ssdut411.app.bookbar.activity.mainPage;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.system.BaseActivity;

/**
 * Created by yao_han on 2016/10/29.
 */
public class SearchActivity extends BaseActivity {
    @Override
    protected String initTitle() {
        return "搜索图书";
    }

    @Override
    protected int initMenu() {
        return 0;
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_search;
    }

    @Override
    protected void initViews() {
//        getEditText(R.id.et_search_input).
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void showView() {

    }
}
