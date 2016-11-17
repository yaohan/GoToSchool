package com.ssdut411.app.bookbar.activity.mainPage;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.system.BaseActivity;
import com.ssdut411.app.bookbar.model.BookModel;
import com.ssdut411.app.bookbar.model.Req.SearchReq;
import com.ssdut411.app.bookbar.model.Resp.SearchResp;
import com.ssdut411.app.bookbar.utils.KeyBoardUtils;
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
 * Created by yao_han on 2016/10/29.
 */
public class SearchActivity extends BaseActivity {
    private EditText etSearch;
    private CommonAdapter adapter;
    private List<BookModel> bookModelList;

    @Override
    protected String initTitle() {
        return "搜索图书";
    }

    @Override
    protected int initMenu() {
        return 0;
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_search;
    }

    @Override
    protected void initViews() {
        etSearch = getEditText(R.id.et_search_input);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    getTextView(R.id.tv_search_close).setVisibility(View.VISIBLE);
                    getButton(R.id.bt_search_search).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    getButton(R.id.bt_search_search).setEnabled(true);
                } else {
                    getTextView(R.id.tv_search_close).setVisibility(View.GONE);
                    getButton(R.id.bt_search_search).setBackgroundColor(getResources().getColor(R.color.font_gray));
                    getButton(R.id.bt_search_search).setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getTextView(R.id.tv_search_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditText(R.id.et_search_input).setText("");
            }
        });
        bookModelList = new ArrayList<>();
        adapter = new CommonAdapter<BookModel>(context, bookModelList, R.layout.item_book_list) {
            @Override
            public void convert(ViewHolder viewHolder, BookModel bookModel, int position) {
                viewHolder.getTextView(R.id.tv_book_name).setText(bookModel.getTitle());
                viewHolder.getTextView(R.id.tv_book_press).setText(bookModel.getPublisher());
                viewHolder.getTextView(R.id.tv_book_time).setText(bookModel.getAuthor());
                VolleyUtil.displayImage(bookModel.getUrl(), viewHolder.getImageView(R.id.iv_book_image), R.mipmap.ic_launcher, R.mipmap.ic_launcher);
            }
        };
        getListView(R.id.lv_search_list).setAdapter(adapter);
        getListView(R.id.lv_search_list).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra("result", bookModelList.get(position).getIsbn13());
                startActivity(intent);
                finish();
                KeyBoardUtils.closeKeyboard(etSearch,context);
            }
        });
        getButton(R.id.bt_search_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.i("onClick start");
                AppAction action = new AppActionImpl(context);
                SearchReq searchReq = new SearchReq();
                searchReq.setKeyWord(getEditText(R.id.et_search_input).getText().toString());
                L.i("search start");
                action.search(searchReq, new ActionCallbackListener<SearchResp>() {
                    @Override
                    public void onSuccess(SearchResp data) {
                        L.i("onSuccess start");
                        if (data.isStatus()) {
                            if(data.getList().size() == 0){
                                T.showShort(context,"未找到相关书籍");
                            }else{
                                bookModelList.clear();
                                bookModelList.addAll(data.getList());
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            T.showShort(context, data.getDesc());
                        }

                    }

                    @Override
                    public void onFailure(String message) {
                        L.i("onFailure start");
                        T.showShort(context,getString(R.string.error_message));
                    }
                });

            }
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void showView() {
        mToolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                KeyBoardUtils.closeKeyboard(etSearch, context);  //关闭软键盘
            }
        });
        KeyBoardUtils.openKeyboard(etSearch, context);//打开软键盘
    }
}
