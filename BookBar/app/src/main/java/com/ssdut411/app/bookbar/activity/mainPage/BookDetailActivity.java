package com.ssdut411.app.bookbar.activity.mainPage;

import android.content.Intent;
import android.view.View;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.person.LoginActivity;
import com.ssdut411.app.bookbar.activity.show.FindBookActivity;
import com.ssdut411.app.bookbar.activity.system.BaseActivity;
import com.ssdut411.app.bookbar.activity.system.MainApplication;
import com.ssdut411.app.bookbar.model.Book;
import com.ssdut411.app.bookbar.model.BookModel;
import com.ssdut411.app.bookbar.model.Req.BorrowBookReq;
import com.ssdut411.app.bookbar.model.Req.CollectionBookReq;
import com.ssdut411.app.bookbar.model.Req.CreateBookReq;
import com.ssdut411.app.bookbar.model.Req.GetBookByISBNReq;
import com.ssdut411.app.bookbar.model.Req.ReservationBookReq;
import com.ssdut411.app.bookbar.model.Resp.BorrowBookResp;
import com.ssdut411.app.bookbar.model.Resp.CollectionBookResp;
import com.ssdut411.app.bookbar.model.Resp.CreateBookResp;
import com.ssdut411.app.bookbar.model.Resp.GetBookByISBNResp;
import com.ssdut411.app.bookbar.model.Resp.ReservationBookResp;
import com.ssdut411.app.bookbar.utils.L;
import com.ssdut411.app.bookbar.utils.T;
import com.ssdut411.app.bookbar.volley.VolleyUtil;
import com.ssdut411.app.bookbar.volley.api.ApiCallbackListener;
import com.ssdut411.app.bookbar.volley.core.ActionCallbackListener;
import com.ssdut411.app.bookbar.volley.core.AppAction;
import com.ssdut411.app.bookbar.volley.core.AppActionImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by LENOVO on 2016/10/29.
 */
public class BookDetailActivity extends BaseActivity {
    public static int REQUEST_CODE = 1;
    private Book book;
    AppAction action = new AppActionImpl(context);
    private BookModel bookModel;
    private boolean canBorrow = false;
    private String result;
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
        getButton(R.id.bt_detail_borrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainApplication.getInstance().getUserId() == null){ //未登录
                    startActivity(new Intent(context,LoginActivity.class));
                }else {
                    if(canBorrow){
//                        startActivityForResult(new Intent(context, FindBookActivity.class), REQUEST_CODE);
//                        finish();
                        BorrowBookReq borrowBookReq = new BorrowBookReq();
                        borrowBookReq.setUserId(MainApplication.getInstance().getUserId());
                        borrowBookReq.setBookId(bookModel.getBookId());
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                        borrowBookReq.setTime(simpleDateFormat.format(new Date()));
                        action.borrowBook(borrowBookReq, new ActionCallbackListener<BorrowBookResp>() {
                            @Override
                            public void onSuccess(BorrowBookResp data) {
                                if(data.isStatus()){
                                    T.showShort(context,data.getDesc());
                                }else{
                                    T.showShort(context,data.getDesc());
                                }
                            }

                            @Override
                            public void onFailure(String message) {
                                T.showShort(context,message);
                            }
                        });
                    }else{
                        ReservationBookReq reservationBookReq = new ReservationBookReq();
                        reservationBookReq.setUserId(MainApplication.getInstance().getUserId());
                        reservationBookReq.setBookId(bookModel.getBookId());
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                        reservationBookReq.setTime(simpleDateFormat.format(new Date()));
                        action.reservationBook(reservationBookReq, new ActionCallbackListener<ReservationBookResp>() {
                            @Override
                            public void onSuccess(ReservationBookResp data) {
                                if (data.isStatus()) {
                                    T.showShort(context, data.getDesc());
                                } else {
                                    T.showShort(context, data.getDesc());
                                }
                            }

                            @Override
                            public void onFailure(String message) {
                                T.showShort(context, message);
                            }
                        });
                    }
                }
            }
        });
        getButton(R.id.bt_detail_collection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainApplication.getInstance().getUserId() == null){
                    startActivity(new Intent(context, LoginActivity.class));
                }else{
                    if(bookModel == null){
                        T.showShort(context,"未知错误");
                    }else{
                        CollectionBookReq collectionBookReq = new CollectionBookReq();
                        collectionBookReq.setUserId(MainApplication.getInstance().getUserId());
                        collectionBookReq.setBookId(bookModel.getBookId());
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                        collectionBookReq.setTime(simpleDateFormat.format(new Date()));
                        action.collectionBook(collectionBookReq, new ActionCallbackListener<CollectionBookResp>() {
                            @Override
                            public void onSuccess(CollectionBookResp data) {
                                if(data.isStatus()){
                                    T.showShort(context,data.getDesc());
                                }else{
                                    T.showShort(context,data.getDesc());
                                }
                            }

                            @Override
                            public void onFailure(String message) {
                                T.showShort(context,message);
                            }
                        });
                    }
                }

            }
        });
        getButton(R.id.bt_detail_navigation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, FindBookActivity.class));
            }
        });
        getButton(R.id.bt_book_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBookInfo(result);
            }
        });

    }

    @Override
    protected void loadData() {
        result = getIntent().getStringExtra("result");
        L.i("result:" + result);
        if(result !=null && result.length()>0){ //扫码
            getBookInfoById(result);
        }else{
            T.showShort(context,"未知错误");
            finish();
        }
    }

    private void getBookInfoById(String isbn) {
        GetBookByISBNReq getBookByISBNReq = new GetBookByISBNReq();
        getBookByISBNReq.setIsbn(isbn);
        action.getBookByISBN(getBookByISBNReq, new ActionCallbackListener<GetBookByISBNResp>() {
            @Override
            public void onSuccess(GetBookByISBNResp data) {
                if(data.isStatus()){
                    bookModel = data.getBook();
                    if(bookModel!=null){
                        getTextView(R.id.tv_book_name).setText(bookModel.getTitle());
                        getTextView(R.id.tv_book_publisher).setText(bookModel.getPublisher());
                        getTextView(R.id.tv_book_author).setText(bookModel.getAuthor());
                        getTextView(R.id.tv_book_content).setText(bookModel.getSummary());
                        VolleyUtil.displayImage(bookModel.getUrl(), getImageView(R.id.iv_book_image), R.mipmap.ic_launcher, R.mipmap.ic_launcher);
                        if(bookModel.getNumber()>0){
                            getButton(R.id.bt_detail_borrow).setText("借阅");
                            canBorrow = true;
                            getButton(R.id.bt_detail_navigation).setEnabled(true);
                            getButton(R.id.bt_detail_navigation).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        }else{
                            getButton(R.id.bt_detail_borrow).setText("预约");
                            canBorrow = false;
                            getButton(R.id.bt_detail_navigation).setEnabled(false);
                            getButton(R.id.bt_detail_navigation).setBackgroundColor(getResources().getColor(R.color.font_gray));
                        }
                    }
                }else{

                    T.showShort(context,data.getDesc());
                }
            }

            @Override
            public void onFailure(String message) {
                T.showShort(context,message);
            }
        });
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
        VolleyUtil.doGet("https://api.douban.com/v2/book/isbn/" + result, Book.class, context, new ApiCallbackListener<Book>() {
            @Override
            public void onSuccess(Book data) {
                book = data;
                getTextView(R.id.tv_book_name).setText(data.getTitle());
                getTextView(R.id.tv_book_publisher).setText(data.getPublisher());
                getTextView(R.id.tv_book_author).setText(getAuthor(data.getAuthor()));
                getTextView(R.id.tv_book_content).setText(data.getSummary());
                VolleyUtil.displayImage(data.getImages().getLarge(), getImageView(R.id.iv_book_image), R.mipmap.ic_launcher, R.mipmap.ic_launcher);
                if(book!=null){
                    CreateBookReq createBookReq = new CreateBookReq();
                    L.i("name:"+book.getTitle());
                    createBookReq.setIsbn(book.getIsbn13());
                    createBookReq.setBookUrl(book.getImages().getLarge());
                    createBookReq.setName(book.getTitle());
                    createBookReq.setPublisher(book.getPublisher());
                    createBookReq.setAuthor(getAuthor(book.getAuthor()));
                    createBookReq.setSummary(book.getSummary());
                    action.createBook(createBookReq, new ActionCallbackListener<CreateBookResp>() {
                        @Override
                        public void onSuccess(CreateBookResp data) {
                            if(data.isStatus()){
                                T.showShort(context,data.getDesc());
                            }else{
                                T.showShort(context,data.getDesc());
                            }
                        }

                        @Override
                        public void onFailure(String message) {
                            T.showShort(context,message);
                        }
                    });
                }else{
                    T.showShort(context,"未知错误");
                }
//                getBookInfoById(data.getIsbn13());
            }

            @Override
            public void onFailure(String message) {
                L.i("error");
            }
        });
    }

}
