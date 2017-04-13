package com.ssdut411.app.bookbarstatic.activity.book;


import com.ssdut411.app.bookbarstatic.R;
import com.ssdut411.app.bookbarstatic.activity.show.DBFile;
import com.ssdut411.app.bookbarstatic.activity.system.BaseActivity;
import com.ssdut411.app.bookbarstatic.model.BookModel;
import com.ssdut411.app.bookbarstatic.utils.L;
import com.ssdut411.app.bookbarstatic.utils.T;
import com.ssdut411.app.bookbarstatic.volley.VolleyUtil;

/**
 * Created by yao_han on 2016/10/30.
 */
public class BookReservationActivity extends BaseActivity {
    private BookModel bookModel;
    private String bookId;
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
        return R.layout.activity_book_reservation;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void loadData() {
        bookId = getIntent().getStringExtra("isbn");
        L.i("bookId:" + bookId);
        if(bookId !=null && bookId.length()>0){
            getBookInfoById(bookId);
        }else{
            T.showShort(context, getString(R.string.error_message));
            finish();
        }
    }
    private void getBookInfoById(String bookId) {
        bookModel = DBFile.getInstance().getBorrowByISBN(bookId);
        L.i("bookModel:" + bookModel);
        if(bookModel == null){
            T.showShort(context, getString(R.string.error_message));
            finish();
        }else{
            getTextView(R.id.tv_book_name).setText(bookModel.getTitle());
            getTextView(R.id.tv_book_publisher).setText(bookModel.getPublisher());
            getTextView(R.id.tv_book_author).setText(bookModel.getAuthor());
            getTextView(R.id.tv_book_content).setText(bookModel.getSummary());
            getTextView(R.id.tv_book_time).setText("预约时间： " + bookModel.getTime());
            VolleyUtil.displayImage(bookModel.getUrl(), getImageView(R.id.iv_book_image), R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        }
    }
    @Override
    protected void showView() {
        setCanBack();
    }
}
