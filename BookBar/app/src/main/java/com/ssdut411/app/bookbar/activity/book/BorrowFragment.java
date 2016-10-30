package com.ssdut411.app.bookbar.activity.book;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.system.BaseFragment;
import com.ssdut411.app.bookbar.model.Book;
import com.ssdut411.app.bookbar.utils.L;
import com.ssdut411.app.bookbar.widget.CommonAdapter;
import com.ssdut411.app.bookbar.widget.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LENOVO on 2016/10/29.
 */
public class BorrowFragment extends BaseFragment {
    @Override
    protected void initView(View view) {
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book());
        getListView(view,R.id.lv_borrow_list).setAdapter(new CommonAdapter<Book>(getActivity(), bookList, R.layout.item_book_list) {
            @Override
            public void convert(ViewHolder viewHolder, Book book, int position) {
                viewHolder.getTextView(R.id.tv_book_name).setText("C语言从入门到精通");
                viewHolder.getTextView(R.id.tv_book_press).setText("出版社：清华大学出版社");
                viewHolder.getTextView(R.id.tv_book_time).setText("离到期还有5天");
            }
        });
        getListView(view,R.id.lv_borrow_list).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                L.i("borrow");
                startActivity(new Intent(getActivity(),BookBorrowActivity.class));
            }
        });
    }

    @Override
    protected int initContentView() {
        return R.layout.fragment_borrow;
    }
}
