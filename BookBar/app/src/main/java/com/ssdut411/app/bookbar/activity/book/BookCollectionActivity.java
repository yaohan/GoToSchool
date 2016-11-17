package com.ssdut411.app.bookbar.activity.book;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.mainPage.BookDetailActivity;
import com.ssdut411.app.bookbar.activity.system.BaseActivity;
import com.ssdut411.app.bookbar.model.BookModel;
import com.ssdut411.app.bookbar.model.Req.GetBookByISBNReq;
import com.ssdut411.app.bookbar.model.Req.GetBookByIdReq;
import com.ssdut411.app.bookbar.model.Resp.GetBookByISBNResp;
import com.ssdut411.app.bookbar.model.Resp.GetBookByIdResp;
import com.ssdut411.app.bookbar.utils.KeyBoardUtils;
import com.ssdut411.app.bookbar.utils.L;
import com.ssdut411.app.bookbar.utils.T;
import com.ssdut411.app.bookbar.volley.VolleyUtil;
import com.ssdut411.app.bookbar.volley.core.ActionCallbackListener;
import com.ssdut411.app.bookbar.volley.core.AppAction;
import com.ssdut411.app.bookbar.volley.core.AppActionImpl;
import com.ssdut411.app.bookbar.widget.CommonAdapter;
import com.ssdut411.app.bookbar.widget.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yao_han on 2016/10/30.
 */
public class BookCollectionActivity extends BaseActivity {
    private BookModel bookModel;
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
        return R.layout.activity_book_collection;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void loadData() {
        String bookId = getIntent().getStringExtra("bookId");
        L.i("bookId:"+bookId);
        if(bookId !=null && bookId.length()>0){
            getBookInfoById(bookId);
        }else{
            T.showShort(context,getString(R.string.error_message));
            finish();
        }
    }

    @Override
    protected void showView() {
        setCanBack();
    }
    private void getBookInfoById(String bookId) {
        AppAction action = new AppActionImpl(context);
        GetBookByIdReq getBookByIdReq = new GetBookByIdReq();
        getBookByIdReq.setBookId(bookId);
        action.getBookById(getBookByIdReq, new ActionCallbackListener<GetBookByIdResp>() {
            @Override
            public void onSuccess(GetBookByIdResp data) {
                if (data.isStatus()) {
                    bookModel = data.getBook();
                    getTextView(R.id.tv_book_name).setText(bookModel.getTitle());
                    getTextView(R.id.tv_book_publisher).setText(bookModel.getPublisher());
                    getTextView(R.id.tv_book_author).setText(bookModel.getAuthor());
                    getTextView(R.id.tv_book_content).setText(bookModel.getSummary());
                    L.i("time:" + bookModel.getTime());
                    getTextView(R.id.tv_book_time).setText("收藏时间：" + getIntent().getStringExtra("time"));
                    VolleyUtil.displayImage(bookModel.getUrl(), getImageView(R.id.iv_book_image), R.mipmap.ic_launcher, R.mipmap.ic_launcher);
                } else {
                    T.showShort(context, data.getDesc());
                }
            }

            @Override
            public void onFailure(String message) {

                T.showShort(context,getString(R.string.error_message));
            }
        });
    }
}
