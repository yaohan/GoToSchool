package com.ssdut411.app.bookbar.activity.book;

import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Log;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.system.BaseActivity;
import com.ssdut411.app.bookbar.model.Book;
import com.ssdut411.app.bookbar.utils.GsonUtils;
import com.ssdut411.app.bookbar.utils.L;
import com.ssdut411.app.bookbar.volley.ApiCallbackListener;
import com.ssdut411.app.bookbar.volley.VolleyUtil;

import java.util.List;

/**
 * Created by LENOVO on 2016/10/29.
 */
public class BookDetailActivity extends BaseActivity {
    private Object bookInfo;

    @Override
    protected String initTitle() {
        return "书籍详情";
    }

    @Override
    protected int initMenu() {
        return 0;
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_book_detail;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void loadData() {
        String result = getIntent().getStringExtra("result");
        if(result !=null && result.length()>0){
            getBookInfo(result);
        }
    }

    private String getAuthor(List<String> author) {
        StringBuffer stringBuffer = new StringBuffer();
        Boolean first = true;
        for(String aut:author){
            if(!first){
                first = false;
                stringBuffer.append(",");
            }
            stringBuffer.append(aut);
        }
        return stringBuffer.toString();
    }

    @Override
    protected void showView() {
        setCanBack();
    }

    public void getBookInfo(String result) {
        VolleyUtil.doGet("https://api.douban.com/v2/book/isbn/" + result,Book.class, context, new ApiCallbackListener<Book>() {
        @Override
        public void onSuccess(Book data) {
            L.i("Book:"+ GsonUtils.gsonToJsonString(data));
//                getImageView(R.id.iv_book_image)
            getTextView(R.id.tv_book_name).setText(data.getTitle());
            getTextView(R.id.tv_book_publisher).setText(data.getPublisher());
            getTextView(R.id.tv_book_author).setText(getAuthor(data.getAuthor()));
            getTextView(R.id.tv_book_content).setText(data.getSummary());
        }

        @Override
        public void onFailure(String message) {
            L.i("error");
        }
    });
    }
}
