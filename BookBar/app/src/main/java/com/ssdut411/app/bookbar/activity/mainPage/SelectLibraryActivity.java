package com.ssdut411.app.bookbar.activity.mainPage;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.system.BaseActivity;
import com.ssdut411.app.bookbar.model.Library;
import com.ssdut411.app.bookbar.model.Req.GetLibraryReq;
import com.ssdut411.app.bookbar.model.Resp.GetLibraryResp;
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
        AppAction action = new AppActionImpl(context);
        GetLibraryReq getLibraryReq = new GetLibraryReq();
        action.getLibrary(getLibraryReq, new ActionCallbackListener<GetLibraryResp>() {
            @Override
            public void onSuccess(GetLibraryResp data) {
                if(data.isStatus()){
                    libraries.clear();
                    libraries.addAll(data.getList());
                    adapter.notifyDataSetChanged();
                }else{
                    T.showShort(context,data.getDesc());
                }
            }

            @Override
            public void onFailure(String message) {
                T.showShort(context,message);
            }
        });
    }

    @Override
    protected void showView() {
        setCanBack();
    }
}
