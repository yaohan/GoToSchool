package com.ssdut411.app.bookbarstatic.activity.book;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.ssdut411.app.bookbarstatic.R;
import com.ssdut411.app.bookbarstatic.activity.person.LoginActivity;
import com.ssdut411.app.bookbarstatic.activity.show.DBFile;
import com.ssdut411.app.bookbarstatic.activity.system.BaseFragment;
import com.ssdut411.app.bookbarstatic.activity.system.MainApplication;
import com.ssdut411.app.bookbarstatic.model.BookModel;
import com.ssdut411.app.bookbarstatic.utils.GsonUtils;
import com.ssdut411.app.bookbarstatic.utils.L;
import com.ssdut411.app.bookbarstatic.volley.VolleyUtil;
import com.ssdut411.app.bookbarstatic.widget.CommonAdapter;
import com.ssdut411.app.bookbarstatic.widget.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LENOVO on 2016/10/29.
 */
public class CollectionFragment extends BaseFragment {
    private CommonAdapter adapter;
    private List<BookModel> bookModelList;
    private ListView lvCollection;
    private LinearLayout llInfo;
    private TextView tvLogin,tvInfo;
    @Override
    protected void initView(View view) {
        llInfo = getLinearLayout(view, R.id.ll_not_login);
        tvLogin = getTextView(view, R.id.tv_login);
        tvInfo = getTextView(view, R.id.tv_info);
        lvCollection = getListView(view, R.id.lv_collection_list);
        bookModelList = DBFile.getInstance().getCollections();
        L.i("collections:"+ GsonUtils.gsonToJsonString(bookModelList));
        if(bookModelList.size() == 0){
            llInfo.setVisibility(View.VISIBLE);
            tvLogin.setVisibility(View.GONE);
            tvInfo.setText("您还未收藏任何图书");
        }else{
            llInfo.setVisibility(View.GONE);
        }
        adapter = new CommonAdapter<BookModel>(getActivity(), bookModelList, R.layout.item_book_list) {
            @Override
            public void convert(ViewHolder viewHolder, BookModel bookModel, int position) {
                viewHolder.getTextView(R.id.tv_book_name).setText(bookModel.getTitle());
                viewHolder.getTextView(R.id.tv_book_press).setText(bookModel.getPublisher());
                viewHolder.getTextView(R.id.tv_book_time).setText("收藏时间： " + bookModel.getTime());
                VolleyUtil.displayImage(bookModel.getUrl(), viewHolder.getImageView(R.id.iv_book_image), R.mipmap.ic_launcher, R.mipmap.ic_launcher);
            }
        };
        lvCollection.setAdapter(adapter);
        lvCollection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BookCollectionActivity.class);
                intent.putExtra("isbn", bookModelList.get(position).getIsbn13());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        bookModelList = DBFile.getInstance().getCollections();
        L.i("collection size:"+bookModelList.size());
        if(bookModelList.size() == 0){
            llInfo.setVisibility(View.VISIBLE);
            tvLogin.setVisibility(View.GONE);
            tvInfo.setText("您还未收藏任何图书");
        }else{
            llInfo.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    protected int initContentView() {
        return R.layout.fragment_collection;
    }
}
