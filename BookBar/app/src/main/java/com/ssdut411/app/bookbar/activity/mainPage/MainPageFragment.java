package com.ssdut411.app.bookbar.activity.mainPage;

import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.system.BaseFragment;
import com.ssdut411.app.bookbar.activity.system.MainActivity;
import com.ssdut411.app.bookbar.model.Book;
import com.ssdut411.app.bookbar.model.Req.GetRecommendBooksReq;
import com.ssdut411.app.bookbar.model.Resp.GetRecommendBooksResp;
import com.ssdut411.app.bookbar.utils.GsonUtils;
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
 * Created by LENOVO on 2016/10/28.
 */
public class MainPageFragment extends BaseFragment {
    private TextView tvLibraryName;
    private GridView gridView;
    private CommonAdapter adapter;
    private List<Book> bookList;

    @Override
    protected void initView(final View view) {
        bookList = new ArrayList<>();
        adapter = new CommonAdapter<Book>(getActivity(), bookList, R.layout.item_main_grid) {
            @Override
            public void convert(ViewHolder viewHolder, Book book, int position) {
                viewHolder.getTextView(R.id.tv_book_name).setText(book.getTitle());
                VolleyUtil.displayImage(book.getUrl(),viewHolder.getImageView(R.id.iv_book_image),R.mipmap.ic_launcher,R.mipmap.ic_launcher);
            }
        };
        getGridView(view, R.id.gv_main_list).setAdapter(adapter);
        getGridView(view, R.id.gv_main_list).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BookDetailActivity.class);
                intent.putExtra("result", bookList.get(position).getIsbn13());
                getActivity().startActivity(intent);
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
                getActivity().startActivityForResult(new Intent(getActivity(), SelectLibraryActivity.class), MainActivity.REQUEST_CODE);
            }
        });
        getLinearLayout(view, R.id.ll_main_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CaptureActivity.class));
            }
        });
        tvLibraryName = getTextView(view, R.id.tv_main_library);
    }

    @Override
    protected int initContentView() {
        return R.layout.fragment_mainpage;
    }

    public void setTextStr(String str) {
        tvLibraryName.setText(str);
    }

    @Override
    public void onResume() {
        super.onResume();
        GetRecommendBooksReq getRecommendBooksReq = new GetRecommendBooksReq();
        AppAction action = new AppActionImpl(getActivity());
        action.getRecommendBooks(getRecommendBooksReq, new ActionCallbackListener<GetRecommendBooksResp>() {
            @Override
            public void onSuccess(GetRecommendBooksResp data) {
                if (data.isStatus()) {
                    bookList.removeAll(bookList);
                    bookList.addAll(data.getList());
                    adapter.notifyDataSetChanged();
                } else {
                    T.showShort(getActivity(), data.getDesc());
                }
            }

            @Override
            public void onFailure(String message) {

                T.showShort(getActivity(),getString(R.string.error_message));
            }
        });
    }
}
