package com.ssdut411.app.bookbar.activity.mainPage;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

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
        getEditText(R.id.et_search_input).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count>0){
                    getTextView(R.id.tv_search_close).setVisibility(View.VISIBLE);
                    getButton(R.id.bt_search_search).setVisibility(View.VISIBLE);
                }else{
                    getTextView(R.id.tv_search_close).setVisibility(View.GONE);
                    getButton(R.id.bt_search_search).setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getTextView(R.id.tv_search_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditText(R.id.et_search_input).setText("");
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
