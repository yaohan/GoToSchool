package com.ssdut411.app.bookbar.activity.book;

import android.view.View;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.system.BaseActivity;
import com.ssdut411.app.bookbar.model.BookModel;
import com.ssdut411.app.bookbar.model.Req.GetBookByIdReq;
import com.ssdut411.app.bookbar.model.Req.ReturnBookReq;
import com.ssdut411.app.bookbar.model.Resp.GetBookByIdResp;
import com.ssdut411.app.bookbar.model.Resp.ReturnBookResp;
import com.ssdut411.app.bookbar.utils.L;
import com.ssdut411.app.bookbar.utils.T;
import com.ssdut411.app.bookbar.volley.VolleyUtil;
import com.ssdut411.app.bookbar.volley.core.ActionCallbackListener;
import com.ssdut411.app.bookbar.volley.core.AppAction;
import com.ssdut411.app.bookbar.volley.core.AppActionImpl;

/**
 * Created by yao_han on 2016/10/30.
 */
public class BookBorrowActivity extends BaseActivity {
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
        return R.layout.activity_book_borrow;
    }

    @Override
    protected void initViews() {
        getButton(R.id.bt_borrow_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppAction action = new AppActionImpl(context);
                ReturnBookReq returnBookReq = new ReturnBookReq();
                L.i("borrowId:"+getIntent().getStringExtra("borrowId"));
                returnBookReq.setBorrowId(getIntent().getStringExtra("borrowId"));
                returnBookReq.setBookId(bookModel.getBookId() + "");
                action.returnBook(returnBookReq, new ActionCallbackListener<ReturnBookResp>() {
                    @Override
                    public void onSuccess(ReturnBookResp data) {
                        if(data.isStatus()){//还书
                            finish();
                        }else{
                            T.showShort(context,data.getDesc());
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        T.showShort(context,getString(R.string.error_message));
                    }
                });
            }
        });
    }

    @Override
    protected void loadData() {
        String bookId = getIntent().getStringExtra("bookId");
        if(bookId !=null && bookId.length()>0){
            getBookInfoById(bookId);
        }else{
            T.showShort(context,getString(R.string.error_message));
            finish();
        }
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
                    L.i("time:"+bookModel.getTime());
                    getTextView(R.id.tv_book_time).setText("借阅时间："+getIntent().getStringExtra("time"));
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
    @Override
    protected void showView() {
        setCanBack();
    }
}
