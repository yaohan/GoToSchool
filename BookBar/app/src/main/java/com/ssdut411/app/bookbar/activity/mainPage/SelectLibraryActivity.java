package com.ssdut411.app.bookbar.activity.mainPage;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.system.BaseActivity;
import com.ssdut411.app.bookbar.model.Library;
import com.ssdut411.app.bookbar.utils.L;
import com.ssdut411.app.bookbar.widget.CommonAdapter;
import com.ssdut411.app.bookbar.widget.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LENOVO on 2016/10/29.
 */
public class SelectLibraryActivity extends BaseActivity {
    @Override
    protected String initTitle() {
        return "切换图书馆";
    }

    @Override
    protected int initMenu() {
        return 0;
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_select_library;
    }

    @Override
    protected void initViews() {
        final List<Library> libraries = new ArrayList<>();
        libraries.add(new Library(R.mipmap.bochuan,"大连理工大学伯川图书馆"));
        libraries.add(new Library(R.mipmap.lingxi,"大连理工大学令希图书馆"));
        libraries.add(new Library(R.mipmap.ruanyuan,"大连理工大学开发区图书馆"));
        getListView(R.id.lv_library_list).setAdapter(new CommonAdapter<Library>(context, libraries, R.layout.item_library) {
            @Override
            public void convert(ViewHolder viewHolder, Library library, int position) {
                viewHolder.getImageView(R.id.iv_library_img).setImageResource(library.getImg());
                viewHolder.getTextView(R.id.tv_library_name).setText(library.getName());
            }
        });
        getListView(R.id.lv_library_list).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= getIntent();
                intent.putExtra("name",((TextView)view.findViewById(R.id.tv_library_name)).getText());
                setResult(RESULT_OK, intent);
                L.i("setResult");
                finish();
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void showView() {
        setCanBack();
    }
}
