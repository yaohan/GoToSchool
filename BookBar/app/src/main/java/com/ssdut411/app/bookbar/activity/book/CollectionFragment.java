package com.ssdut411.app.bookbar.activity.book;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.mainPage.BookDetailActivity;
import com.ssdut411.app.bookbar.activity.person.LoginActivity;
import com.ssdut411.app.bookbar.activity.system.BaseFragment;
import com.ssdut411.app.bookbar.activity.system.MainApplication;
import com.ssdut411.app.bookbar.model.Book;
import com.ssdut411.app.bookbar.model.BookModel;
import com.ssdut411.app.bookbar.model.Req.GetCollectionReq;
import com.ssdut411.app.bookbar.model.Resp.GetCollectionResp;
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
        llInfo = getLinearLayout(view,R.id.ll_not_login);
        tvLogin = getTextView(view, R.id.tv_login);
        tvInfo = getTextView(view,R.id.tv_info);
        lvCollection = getListView(view,R.id.lv_collection_list);
        bookModelList = new ArrayList<>();
        adapter = new CommonAdapter<BookModel>(getActivity(), bookModelList, R.layout.item_book_list) {
            @Override
            public void convert(ViewHolder viewHolder, BookModel bookModel, int position) {
                viewHolder.getTextView(R.id.tv_book_name).setText(bookModel.getTitle());
                viewHolder.getTextView(R.id.tv_book_press).setText(bookModel.getPublisher());
                viewHolder.getTextView(R.id.tv_book_time).setText("收藏时间"+bookModel.getTime());
                VolleyUtil.displayImage(bookModel.getUrl(), viewHolder.getImageView(R.id.iv_book_image), R.mipmap.ic_launcher, R.mipmap.ic_launcher);
            }
        };
        lvCollection.setAdapter(adapter);
        lvCollection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BookCollectionActivity.class);
                L.i("put bookId:"+bookModelList.get(position).getBookId());
                intent.putExtra("bookId", bookModelList.get(position).getBookId()+"");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        L.i("Collection onResume");
        AppAction action = new AppActionImpl(getActivity());
        GetCollectionReq getCollectionReq = new GetCollectionReq();
        L.i("userId:"+MainApplication.getInstance().getUserId());
        if(MainApplication.getInstance().getUserId() == null){
            llInfo.setVisibility(View.VISIBLE);
            tvLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            });
            tvLogin.setVisibility(View.VISIBLE);
            tvInfo.setText("以查看收藏记录");
        }else{
            llInfo.setVisibility(View.GONE);
            getCollectionReq.setUserId(MainApplication.getInstance().getUserId());
            action.getCollection(getCollectionReq, new ActionCallbackListener<GetCollectionResp>() {
                @Override
                public void onSuccess(GetCollectionResp data) {
                    if(data.isStatus()){
                        if(data.getList().size() == 0){
                            llInfo.setVisibility(View.VISIBLE);
                            tvLogin.setVisibility(View.GONE);
                            tvInfo.setText("您还未收藏任何图书");
                        }else{
                            bookModelList.clear();
                            bookModelList.addAll(data.getList());
                            adapter.notifyDataSetChanged();
                        }
                    }else{
                        T.showShort(getActivity(),data.getDesc());
                    }
                }

                @Override
                public void onFailure(String message) {
                    T.showShort(getActivity(),message);
                }
            });
        }
    }

    @Override
    protected int initContentView() {
        return R.layout.fragment_collection;
    }
}
