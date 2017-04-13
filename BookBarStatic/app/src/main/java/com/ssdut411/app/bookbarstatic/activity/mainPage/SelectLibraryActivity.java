package com.ssdut411.app.bookbarstatic.activity.mainPage;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ssdut411.app.bookbarstatic.R;
import com.ssdut411.app.bookbarstatic.activity.system.BaseActivity;
import com.ssdut411.app.bookbarstatic.model.Library;
import com.ssdut411.app.bookbarstatic.utils.L;
import com.ssdut411.app.bookbarstatic.widget.CommonAdapter;
import com.ssdut411.app.bookbarstatic.widget.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LENOVO on 2016/10/29.
 */
public class SelectLibraryActivity extends BaseActivity {
    private CommonAdapter adapter;
    private List<Library> libraries;
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
        libraries = new ArrayList<>();
        Library library = new Library();
        library.setName("测试图书馆1");
        Library library1 = new Library();
        library1.setName("测试图书馆2");
        Library library2 = new Library();
        library2.setName("测试图书馆3");
        libraries.add(library);
        libraries.add(library1);
        libraries.add(library2);
        adapter = new CommonAdapter<Library>(context, libraries, R.layout.item_library) {
            @Override
            public void convert(ViewHolder viewHolder, Library library, int position) {
//                VolleyUtil.displayImage(library.getUrl(), viewHolder.getImageView(R.id.iv_library_img), R.mipmap.ic_launcher, R.mipmap.ic_launcher);
                viewHolder.getImageView(R.id.iv_library_img).setImageResource(R.mipmap.ic_launcher);
                viewHolder.getTextView(R.id.tv_library_name).setText(library.getName());
            }
        };
        getListView(R.id.lv_library_list).setAdapter(adapter);
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
