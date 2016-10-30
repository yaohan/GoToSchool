package com.ssdut411.app.bookbar.activity.book;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.system.BaseActivity;
import com.ssdut411.app.bookbar.utils.L;

/**
 * Created by yao_han on 2016/10/30.
 */
public class BookBorrowActivity extends BaseActivity {
    @Override
    protected String initTitle() {
        return "图书详情";
    }

    @Override
    protected int initMenu() {
        return 0;
    }

    @Override
    protected int initContentView() {
        L.i("borrow");
        return R.layout.activity_book_borrow;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void showView() {
        setCanBack();
    }
}
