package com.ssdut411.app.bookbar.activity.book;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.person.LoginActivity;
import com.ssdut411.app.bookbar.activity.system.BaseFragment;
import com.ssdut411.app.bookbar.activity.system.MainApplication;
import com.ssdut411.app.bookbar.model.Book;
import com.ssdut411.app.bookbar.model.BookModel;
import com.ssdut411.app.bookbar.model.Req.GetBorrowReq;
import com.ssdut411.app.bookbar.model.Req.GetCollectionReq;
import com.ssdut411.app.bookbar.model.Resp.GetBorrowResp;
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
public class BorrowFragment extends BaseFragment {
    private CommonAdapter adapter;
    private List<BookModel> bookModelList;
    private ListView lvBorrow;
    private LinearLayout llInfo;
    private TextView tvLogin,tvInfo;
    @Override
    protected void initView(View view) {
        llInfo = getLinearLayout(view,R.id.ll_not_login);
        tvLogin = getTextView(view, R.id.tv_login);
        tvInfo = getTextView(view,R.id.tv_info);
        lvBorrow = getListView(view,R.id.lv_borrow_list);
        bookModelList = new ArrayList<>();
        adapter = new CommonAdapter<BookModel>(getActivity(), bookModelList, R.layout.item_book_list) {
            @Override
            public void convert(ViewHolder viewHolder, BookModel bookModel, int position) {
                viewHolder.getTextView(R.id.tv_book_name).setText(bookModel.getTitle());
                viewHolder.getTextView(R.id.tv_book_press).setText(bookModel.getPublisher());
                viewHolder.getTextView(R.id.tv_book_time).setText("借阅时间"+bookModel.getTime());
                VolleyUtil.displayImage(bookModel.getUrl(), viewHolder.getImageView(R.id.iv_book_image), R.mipmap.ic_launcher, R.mipmap.ic_launcher);
            }
        };
        lvBorrow.setAdapter(adapter);
        lvBorrow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BookBorrowActivity.class);
                L.i("put bookId:"+bookModelList.get(position).getBookId());
                L.i("put borrowId:"+bookModelList.get(position).getBorrowId());
                intent.putExtra("borrowId", bookModelList.get(position).getBorrowId());
                intent.putExtra("time",bookModelList.get(position).getTime());
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
        GetBorrowReq getBorrowReq = new GetBorrowReq();
        if(MainApplication.getInstance().getUserId() == null){
            llInfo.setVisibility(View.VISIBLE);
            tvLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            });
            tvLogin.setVisibility(View.VISIBLE);
            tvInfo.setText("以查看借阅记录");
        }else{llInfo.setVisibility(View.GONE);
            getBorrowReq.setUserId(MainApplication.getInstance().getUserId());
            action.getBorrow(getBorrowReq, new ActionCallbackListener<GetBorrowResp>() {
                @Override
                public void onSuccess(GetBorrowResp data) {
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
                        T.showShort(getActivity(), data.getDesc());
                    }
                }

                @Override
                public void onFailure(String message) {
                    T.showShort(getActivity(), getString(R.string.error_message));
                }
            });
        }
    }

    @Override
    protected int initContentView() {
        return R.layout.fragment_borrow;
    }
}
