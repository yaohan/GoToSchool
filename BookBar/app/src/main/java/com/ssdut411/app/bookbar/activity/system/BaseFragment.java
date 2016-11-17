package com.ssdut411.app.bookbar.activity.system;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssdut411.app.bookbar.widget.ViewPagerIndicator;

/**
 * Created by LENOVO on 2016/10/28.
 */
public abstract class BaseFragment extends Fragment {

    private SparseArray<View> views = new SparseArray<View>();
    public BaseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(initContentView(),null);
        initView(view);
        return view;
    }

    protected abstract void initView(View view);

    protected abstract int initContentView();

    /**
     * 简化绑定View
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(View contentView, int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = contentView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }


    public LinearLayout getLinearLayout(View view,int viewId) {
        return getView(view, viewId);
    }
    public ViewPagerIndicator getViewPagerIndicator(View view,int viewId) {
        return getView(view, viewId);
    }
    public ViewPager getViewPager(View view,int viewId) {
        return getView(view, viewId);
    }
    public ListView getListView(View view,int viewId) {
        return getView(view, viewId);
    }
    public GridView getGridView(View view,int viewId) { return getView(view, viewId); }
    public EditText getEditText(View view,int viewId) { return getView(view, viewId); }
    public TextView getTextView(View view,int viewId) { return getView(view, viewId); }
    public RelativeLayout getRelativeLayout(View view, int viewId) { return getView(view,viewId); }
    public ImageView getImageView(View view, int viewId) { return getView(view,viewId); }
    public Button getButton(View view,int viewId) { return getView(view,viewId); }

}
