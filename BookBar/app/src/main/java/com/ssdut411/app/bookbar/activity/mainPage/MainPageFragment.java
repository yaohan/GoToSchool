package com.ssdut411.app.bookbar.activity.mainPage;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.book.BookDetailActivity;
import com.ssdut411.app.bookbar.activity.system.BaseFragment;
import com.ssdut411.app.bookbar.model.Book;
import com.ssdut411.app.bookbar.widget.CommonAdapter;
import com.ssdut411.app.bookbar.widget.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LENOVO on 2016/10/28.
 */
public class MainPageFragment extends BaseFragment {
    @Override
    protected void initView(final View view) {
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book());
        bookList.add(new Book());
        bookList.add(new Book());
        bookList.add(new Book());
        bookList.add(new Book());
        bookList.add(new Book());
        bookList.add(new Book());
        getGridView(view, R.id.gv_main_list).setAdapter(new CommonAdapter<Book>(getActivity(), bookList, R.layout.item_main_grid) {
            @Override
            public void convert(ViewHolder viewHolder, Book book, int position) {
                viewHolder.getTextView(R.id.tv_book_name).setText("C语言从入门到精通");
            }
        });
        getGridView(view,R.id.gv_main_list).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), BookDetailActivity.class));
            }
        });
        getEditText(view, R.id.et_main_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
        getRelativeLayout(view, R.id.rl_main_library).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SelectLibraryActivity.class));
            }
        });
        getImageView(view,R.id.iv_main_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),CaptureActivity.class));
            }
        });
    }

    @Override
    protected int initContentView() {
        return R.layout.fragment_mainpage;
    }
}
