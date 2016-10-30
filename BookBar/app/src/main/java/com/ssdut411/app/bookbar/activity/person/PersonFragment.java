package com.ssdut411.app.bookbar.activity.person;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.system.BaseFragment;
import com.ssdut411.app.bookbar.utils.L;
import com.ssdut411.app.bookbar.utils.T;

/**
 * Created by LENOVO on 2016/10/28.
 */
public class PersonFragment extends BaseFragment {
    @Override
    protected void initView(View view) {
        LinearLayout layout = getLinearLayout(view,R.id.ll_person_info);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        getLinearLayout(view,R.id.ll_person_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
            }
        });
        getLinearLayout(view,R.id.ll_person_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(getActivity(), "已经是最新版本了");
            }
        });
        getLinearLayout(view,R.id.ll_person_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(getActivity(),"联系我们");
            }
        });
        getLinearLayout(view,R.id.ll_person_use).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(getActivity(),"使用手册");
            }
        });
    }

    @Override
    protected int initContentView() {
        return R.layout.fragment_person;
    }
}
