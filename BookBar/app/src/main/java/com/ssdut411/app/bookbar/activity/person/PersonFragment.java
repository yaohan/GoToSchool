package com.ssdut411.app.bookbar.activity.person;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.system.BaseFragment;
import com.ssdut411.app.bookbar.utils.L;

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
                startActivity(new Intent(getActivity(),LoginActivity.class));
            }
        });
    }

    @Override
    protected int initContentView() {
        return R.layout.fragment_person;
    }
}
