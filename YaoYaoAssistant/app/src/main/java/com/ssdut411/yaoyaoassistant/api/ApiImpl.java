package com.ssdut411.yaoyaoassistant.api;

import com.android.volley.toolbox.Volley;
import com.ssdut411.yaoyaoassistant.api.volley.VolleyUtil;
import com.ssdut411.yaoyaoassistant.model.resp.BaseResp;
import com.ssdut411.yaoyaoassistant.model.MainInfo;
import com.ssdut411.yaoyaoassistant.model.resp.DetailResp;
import com.ssdut411.yaoyaoassistant.model.resp.ListResp;
import com.ssdut411.yaoyaoassistant.model.resp.MainInfoResp;

/**
 * Created by yao_han on 2017/4/22.
 */
public class ApiImpl implements Api {
    @Override
    public void getMainInfo(String url, String reqJson, Object tag, ApiCallbackListener<MainInfoResp> listener) {
        VolleyUtil.doPost(url,reqJson,MainInfoResp.class,tag,listener);
    }

    @Override
    public void createAccount(String url, String reqJson, Object tag, ApiCallbackListener<BaseResp> listener) {
        VolleyUtil.doPost(url,reqJson,BaseResp.class,tag,listener);
    }

    @Override
    public void createDetails(String url, String reqJson, Object tag, ApiCallbackListener<BaseResp> listener) {
        VolleyUtil.doPost(url,reqJson,BaseResp.class,tag,listener);
    }

    @Override
    public void getList(String url, String reqJson, Object tag, ApiCallbackListener<ListResp> listener) {
        VolleyUtil.doPost(url,reqJson,ListResp.class,tag,listener);
    }

    @Override
    public void createTransfer(String url, String reqJson, Object tag, ApiCallbackListener<BaseResp> listener) {
        VolleyUtil.doPost(url,reqJson,BaseResp.class,tag,listener);
    }

    @Override
    public void getDetails(String url, String reqJson, Object tag, ApiCallbackListener<DetailResp> listener) {
        VolleyUtil.doPost(url,reqJson,DetailResp.class,tag,listener);
    }

}
