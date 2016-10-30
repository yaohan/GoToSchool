package com.ssdut411.app.bookbar.activity.mainPage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.book.BookDetailActivity;
import com.ssdut411.app.bookbar.activity.system.BaseFragment;
import com.ssdut411.app.bookbar.activity.system.MainActivity;
import com.ssdut411.app.bookbar.model.Book;
import com.ssdut411.app.bookbar.utils.L;
import com.ssdut411.app.bookbar.widget.CommonAdapter;
import com.ssdut411.app.bookbar.widget.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LENOVO on 2016/10/28.
 */
public class MainPageFragment extends BaseFragment {
    private Handler handler;
    private TextView tvLibraryName;
    private View view;

    public static MainPageFragment Instance(String libraryName){
        MainPageFragment fragment = new MainPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", libraryName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(final View view) {
        this.view = view;
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
        getGridView(view, R.id.gv_main_list).setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                L.i("request code:" + MainActivity.REQUEST_CODE);
                getActivity().startActivityForResult(new Intent(getActivity(), SelectLibraryActivity.class), MainActivity.REQUEST_CODE);
            }
        });
        getImageView(view, R.id.iv_main_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CaptureActivity.class));
            }
        });
        tvLibraryName = (TextView)view.findViewById(R.id.tv_main_library);
        L.i("initView tv:" + (tvLibraryName == null));
    }

    @Override
    public void onStart() {
//        tvLibraryName = getTextView(view,R.id.tv_library_name);
//        L.i("onStart tv:"+(tvLibraryName == null));
        super.onStart();
    }

    @Override
    protected int initContentView() {
        return R.layout.fragment_mainpage;
    }

    public void setHandler(Handler handler){
        this.handler = handler;
    }
    public void setTextStr(String str){
        tvLibraryName.setText(str);
    }

}
