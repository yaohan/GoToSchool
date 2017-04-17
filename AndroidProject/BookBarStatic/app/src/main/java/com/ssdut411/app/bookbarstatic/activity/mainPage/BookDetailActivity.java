package com.ssdut411.app.bookbarstatic.activity.mainPage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.ssdut411.app.bookbarstatic.activity.person.LoginActivity;
import com.ssdut411.app.bookbarstatic.activity.show.DBFile;
import com.ssdut411.app.bookbarstatic.activity.show.FindBookActivity;
import com.ssdut411.app.bookbarstatic.activity.show.Location;
import com.ssdut411.app.bookbarstatic.activity.show.WifiInfo;
import com.ssdut411.app.bookbarstatic.activity.system.BaseActivity;
import com.ssdut411.app.bookbarstatic.activity.system.MainApplication;
import com.ssdut411.app.bookbarstatic.model.Book;
import com.ssdut411.app.bookbarstatic.model.BookModel;
import com.ssdut411.app.bookbarstatic.utils.L;
import com.ssdut411.app.bookbarstatic.utils.T;
import com.ssdut411.app.bookbarstatic.volley.VolleyUtil;
import com.ssdut411.app.bookbarstatic.volley.api.ApiCallbackListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.ssdut411.app.bookbarstatic.R;

/**
 * Created by LENOVO on 2016/10/29.
 */
public class BookDetailActivity extends BaseActivity {
    private List<WifiInfo> wifiInfoList;
    private float locationX=0,locationY=0;
    private Timer timer = new Timer();
    public static int REQUEST_CODE = 1;
    private Book book;
    private BookModel bookModel;
    private boolean canBorrow = false;
    private String result;
    private ProgressDialog progressDialog;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            T.showShort(context, "定位失败。请先完成训练阶段");
            timer.cancel();
//            finish();
        }
    };

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
        progressDialog = new ProgressDialog(context);
        startScanWIFI();
        getButton(R.id.bt_detail_borrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainApplication.getInstance().getUserId() == null) { //未登录
                    startActivity(new Intent(context, LoginActivity.class));
                } else {
                    if (canBorrow) {
                        if (MainApplication.getInstance().isHasScan()) { //扫码进来的
                            DBFile.getInstance().borrowBook(result,locationX,locationY);
                        } else {//非扫码进来的
                            MainApplication.getInstance().setDirectBorrow(true);
                            startActivity(new Intent(context, CaptureActivity.class));
                        }

                    } else {
                        DBFile.getInstance().reservationBook(result);
                    }
                }
            }
        });
        getButton(R.id.bt_detail_collection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainApplication.getInstance().getUserId() == null) {
                    startActivity(new Intent(context, LoginActivity.class));
                } else {
                    if (bookModel == null) {
                        T.showShort(context, getString(R.string.error_message));
                    } else {
                        if (DBFile.getInstance().collectionBook(result)) {
                        }
                    }
                }
            }
        });
        getButton(R.id.bt_detail_navigation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FindBookActivity.class);
                intent.putExtra("locationX", bookModel.getLocationX());
                intent.putExtra("locationY", bookModel.getLocationY());
                L.i("put x:" + bookModel);
                startActivity(intent);
            }
        });
        getButton(R.id.bt_book_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (book != null) {
                    if (locationX == 0 && locationY == 0) {
                        T.showShort(context, "定位失敗");
                    } else {
                        DBFile.getInstance().addBook(book, locationX, locationY);
                        T.showShort(context, "添加成功");
                        MainApplication.getInstance().setIsAdmin(false);
                        finish();
                    }

                } else {
                    T.showShort(context, getString(R.string.error_message));
                }
            }
        });
    }

    @Override
    protected void loadData() {
        progressDialog.show();
        result = getIntent().getStringExtra("result");
        L.i("result:" + result);
        if (result != null && result.length() > 0) { //扫码
            if(MainApplication.getInstance().isDirectBorrow()){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        DBFile.getInstance().borrowBook(result, locationX, locationY);
                    }
                },1000);
                getBookInfoById(result);
            }else{
                if (MainApplication.getInstance().isAdmin()) {
                    getLinearLayout(R.id.ll_book_button).setVisibility(View.GONE);
                    getLinearLayout(R.id.ll_book_add).setVisibility(View.VISIBLE);
                    getBookInfo(result);
                } else {
                    getBookInfoById(result);
                }
            }
        } else {
            T.showShort(context, getString(R.string.error_message));
            finish();
        }
    }

    private void getBookInfoById(String isbn) {
        bookModel = DBFile.getInstance().getBookByISBN(result);
        if(bookModel == null){
            T.showShort(context, "未找到这本书");
            finish();
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    getTextView(R.id.tv_book_name).setText(bookModel.getTitle());
                    getTextView(R.id.tv_book_number).setText(bookModel.getNumber() + "本");
                    getTextView(R.id.tv_book_publisher).setText(bookModel.getPublisher());
                    getTextView(R.id.tv_book_author).setText(bookModel.getAuthor());
                    getTextView(R.id.tv_book_content).setText(bookModel.getSummary());
                    VolleyUtil.displayImage(bookModel.getUrl(), getImageView(R.id.iv_book_image), R.mipmap.ic_launcher, R.mipmap.ic_launcher);
                    if (bookModel.getNumber() > 0) {
                        getButton(R.id.bt_detail_borrow).setText("借阅");
                        canBorrow = true;
                        getButton(R.id.bt_detail_navigation).setEnabled(true);
                        getButton(R.id.bt_detail_navigation).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        getButton(R.id.bt_detail_borrow).setText("预约");
                        canBorrow = false;
                        getButton(R.id.bt_detail_navigation).setEnabled(false);
                        getButton(R.id.bt_detail_navigation).setBackgroundColor(getResources().getColor(R.color.font_gray));
                    }
                }
            }, 1000);

        }

    }

    private String getAuthor(List<String> author) {
        StringBuffer stringBuffer = new StringBuffer();
        Boolean first = true;
        for (String aut : author) {
            if (!first) {
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
            public void onSuccess(final Book data) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        BookModel bookModel = new BookModel();
                        if(bookModel != null){
                            getTextView(R.id.tv_book_number).setText(bookModel.getNumber()+"");
                        }
                        getTextView(R.id.tv_book_name).setText(data.getTitle());
                        getTextView(R.id.tv_book_publisher).setText(data.getPublisher());
                        getTextView(R.id.tv_book_author).setText(getAuthor(data.getAuthor()));
                        getTextView(R.id.tv_book_content).setText(data.getSummary());
                        VolleyUtil.displayImage(data.getImages().getLarge(), getImageView(R.id.iv_book_image), R.mipmap.ic_launcher, R.mipmap.ic_launcher);
                    }
                }, 1000);
//                getBookInfoById(data.getIsbn13());
            }

            @Override
            public void onFailure(String message) {
                L.i("error");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainApplication.getInstance().setHasScan(false);
        L.i("set directBorrow onDestory" + MainApplication.getInstance().isDirectBorrow());
        MainApplication.getInstance().setDirectBorrow(false);
    }
    private void startScanWIFI() {
        final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        wifiManager.startScan();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                List<ScanResult> list = wifiManager.getScanResults();
                List<WifiInfo> wifiList = new ArrayList<WifiInfo>();
                for (int i = 0; i < list.size(); i++) {
                    WifiInfo wifiInfo = new WifiInfo(list.get(i).BSSID, WifiManager.calculateSignalLevel(list.get(i).level, 100));
                    wifiList.add(wifiInfo);
                }
                wifiInfoList = wifiList;
                Location location = DBFile.getInstance().searchLocation(wifiList);
                if(location == null){
                    handler.sendEmptyMessage(0);
                }else{
                    locationX = location.getX();
                    locationY = location.getY();
                }

            }
        }, 0, 1000);
    }
}
