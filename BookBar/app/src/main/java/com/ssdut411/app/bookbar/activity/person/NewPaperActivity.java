package com.ssdut411.app.bookbar.activity.person;

import android.content.Intent;
import android.view.View;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.mainPage.SelectLibraryActivity;
import com.ssdut411.app.bookbar.activity.system.BaseActivity;
import com.ssdut411.app.bookbar.utils.T;

/**
 * Created by LENOVO on 2016/11/1.
 */
public class NewPaperActivity extends BaseActivity {
    public static int REQUEST_CODE = 2;
    @Override
    protected String initTitle() {
        return "验证证件";
    }

    @Override
    protected int initMenu() {
        return 0;
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_new_paper;
    }

    @Override
    protected void initViews() {
        getTextView(R.id.tv_newpaper_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(context, SelectLibraryActivity.class),REQUEST_CODE);
            }
        });
        getEditText(R.id.et_newpaper_number).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getView(R.id.v_et_newpaper_number).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        getButton(R.id.bt_new_paper).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(context,"确定");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTextView(R.id.tv_newpaper_select).setText(data.getStringExtra("name"));
//        super.onActivityResult(requestCode, resultCode, data);
    }
}
