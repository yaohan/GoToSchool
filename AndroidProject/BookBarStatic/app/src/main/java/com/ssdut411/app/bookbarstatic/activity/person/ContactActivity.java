package com.ssdut411.app.bookbarstatic.activity.person;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.ssdut411.app.bookbarstatic.R;
import com.ssdut411.app.bookbarstatic.activity.system.BaseActivity;
import com.ssdut411.app.bookbarstatic.utils.KeyBoardUtils;
import com.ssdut411.app.bookbarstatic.utils.T;


/**
 * Created by LENOVO on 2016/11/7.
 */
public class ContactActivity extends BaseActivity {
    @Override
    protected String initTitle() {
        return "意见反馈";
    }

    @Override
    protected int initMenu() {
        return R.menu.menu_sure;
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_contact;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void showView() {
        KeyBoardUtils.openKeyboard(getEditText(R.id.et_feedback), context);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(getEditText(R.id.et_feedback).getText().toString().length() == 0){
                    T.showShort(context, "反馈意见不能为空");
                    return false;
                }else{
                    T.showShort(context,"感谢您的反馈意见，我们会尽快解决");
                    KeyBoardUtils.closeKeyboard(getEditText(R.id.et_feedback),context);
                    finish();
                    return true;
                }
            }
        });
        mToolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeyboard(getEditText(R.id.et_feedback), context);
                finish();
            }
        });
    }
}
