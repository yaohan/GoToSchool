package com.ssdut411.app.bookbarstatic.activity.person;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssdut411.app.bookbarstatic.R;
import com.ssdut411.app.bookbarstatic.activity.system.BaseFragment;
import com.ssdut411.app.bookbarstatic.activity.system.MainActivity;
import com.ssdut411.app.bookbarstatic.activity.system.MainApplication;
import com.ssdut411.app.bookbarstatic.utils.ActivityStackUtils;
import com.ssdut411.app.bookbarstatic.utils.T;


/**
 * Created by LENOVO on 2016/10/28.
 */
public class PersonFragment extends BaseFragment {
    private TextView name;
    private Button logout;
    @Override
    protected void initView(View view) {
        name = getTextView(view, R.id.tv_person_name);
        logout = getButton(view,R.id.bt_person_logout);
        LinearLayout layout = getLinearLayout(view,R.id.ll_person_info);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainApplication.getInstance().getUserId() == null){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });
        getLinearLayout(view,R.id.ll_person_paperwork).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PaperWorkActivity.class));
            }
        });
        getLinearLayout(view, R.id.ll_person_change).setOnClickListener(new View.OnClickListener() {
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
                startActivity(new Intent(getActivity(),ContactActivity.class));
            }
        });
        getLinearLayout(view,R.id.ll_person_use).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(getActivity(), "使用手册");
            }
        });
        getLinearLayout(view,R.id.ll_person_train).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AdminActivity.class));
//                startActivity(new Intent(getActivity(), CreateDBActivity.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainApplication.getInstance().clear();
                name.setText("点击登录");
                logout.setVisibility(View.GONE);
                startActivity(new Intent(getActivity(), MainActivity.class));
                ActivityStackUtils.getInstance().exit();
            }
        });
    }

    @Override
    protected int initContentView() {
        return R.layout.fragment_person;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(MainApplication.getInstance().getUserId() != null){
            name.setText(MainApplication.getInstance().getPhoneNumber() + "");
            logout.setVisibility(View.VISIBLE);
        }else{
            name.setText("点击登录");
            logout.setVisibility(View.GONE);
        }
    }
}
