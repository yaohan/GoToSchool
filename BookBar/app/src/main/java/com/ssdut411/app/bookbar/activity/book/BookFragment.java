package com.ssdut411.app.bookbar.activity.book;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.system.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LENOVO on 2016/10/28.
 */
public class BookFragment extends BaseFragment {
    @Override
    protected void initView(View view) {
        ViewPager viewPager = getViewPager(view,R.id.vp_book_viewpager);
        final List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new BorrowFragment());
        fragmentList.add(new ReservationFragment());
        fragmentList.add(new CollectionFragment());
        viewPager.setAdapter(new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        getViewPagerIndicator(view,R.id.vpi_book_indicator).setViewPager(viewPager, 0);
    }

    @Override
    protected int initContentView() {
        return R.layout.fragment_book;
    }
}
