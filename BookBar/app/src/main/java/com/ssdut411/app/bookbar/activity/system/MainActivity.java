package com.ssdut411.app.bookbar.activity.system;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.book.BookFragment;
import com.ssdut411.app.bookbar.activity.mainPage.MainPageFragment;
import com.ssdut411.app.bookbar.activity.person.PersonFragment;
import com.ssdut411.app.bookbar.utils.KeyBoardUtils;
import com.ssdut411.app.bookbar.utils.L;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    public static int REQUEST_CODE = 1;

    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private final MainPageFragment mainPageFragment = new MainPageFragment();
    private FragmentPagerAdapter adapter;

    @Override
    protected String initTitle() {
        return "书吧";
    }

    @Override
    protected int initMenu() {
        return 0;
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        viewPager = getViewPager(R.id.mainpage_viewpager);
        viewPager.setOffscreenPageLimit(3);
        getLinearLayout(R.id.ll_tab_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
                reset();
                getImageView(R.id.iv_tab_main).setImageResource(R.mipmap.icon_library_select);
                getTextView(R.id.tv_tab_main).setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        getLinearLayout(R.id.ll_tab_book).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
                reset();
                getImageView(R.id.iv_tab_book).setImageResource(R.mipmap.icon_book_select);
                getTextView(R.id.tv_tab_book).setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        getLinearLayout(R.id.ll_tab_person).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
                reset();
                getImageView(R.id.iv_tab_person).setImageResource(R.mipmap.icon_person_select);
                getTextView(R.id.tv_tab_person).setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });


    }

    private void reset() {
        getImageView(R.id.iv_tab_main).setImageResource(R.mipmap.icon_library_unselect);
        getImageView(R.id.iv_tab_book).setImageResource(R.mipmap.icon_book_unselect);
        getImageView(R.id.iv_tab_person).setImageResource(R.mipmap.icon_person_unselect);
        getTextView(R.id.tv_tab_main).setTextColor(getResources().getColor(R.color.font_gray));
        getTextView(R.id.tv_tab_book).setTextColor(getResources().getColor(R.color.font_gray));
        getTextView(R.id.tv_tab_person).setTextColor(getResources().getColor(R.color.font_gray));
    }
    @Override
    protected void loadData() {

    }

    @Override
    protected void showView() {
        setBackTips();
        fragmentList =  new ArrayList<>();
        BookFragment bookFragment = new BookFragment();
        PersonFragment personFragment = new PersonFragment();
        fragmentList.add(mainPageFragment);
        fragmentList.add(bookFragment);
        fragmentList.add(personFragment);
        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            mainPageFragment.setTextStr("当前图书馆："+data.getStringExtra("name"));
        }
//        super.onActivityResult(requestCode, resultCode, data);
    }

}
