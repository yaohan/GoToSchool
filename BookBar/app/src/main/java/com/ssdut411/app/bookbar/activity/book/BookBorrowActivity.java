package com.ssdut411.app.bookbar.activity.book;

import android.view.View;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.system.BaseActivity;
import com.ssdut411.app.bookbar.utils.L;
import com.ssdut411.app.bookbar.utils.T;

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
        return R.layout.activity_book_borrow;
    }

    @Override
    protected void initViews() {
        getButton(R.id.bt_borrow_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(context,"还书");
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
