package com.ssdut411.app.bookbarstatic.activity.mainPage;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.ssdut411.app.bookbarstatic.R;
import com.ssdut411.app.bookbarstatic.activity.show.DBFile;
import com.ssdut411.app.bookbarstatic.activity.system.BaseActivity;
import com.ssdut411.app.bookbarstatic.model.BookModel;
import com.ssdut411.app.bookbarstatic.utils.KeyBoardUtils;
import com.ssdut411.app.bookbarstatic.utils.L;
import com.ssdut411.app.bookbarstatic.volley.VolleyUtil;
import com.ssdut411.app.bookbarstatic.widget.CommonAdapter;
import com.ssdut411.app.bookbarstatic.widget.ViewHolder;

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
                KeyBoardUtils.closeKeyboard(etSearch, context);
            }
        });
        getButton(R.id.bt_search_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<BookModel> bookModels = DBFile.getInstance().searchBook(etSearch.getText().toString());
                if(bookModels.size() == 0){
                    getLinearLayout(R.id.hint).setVisibility(View.VISIBLE);
                }else{
                    getLinearLayout(R.id.hint).setVisibility(View.GONE);
                    bookModelList.removeAll(bookModelList);
                    bookModelList.addAll(bookModels);
                    adapter.notifyDataSetChanged();
                }
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
