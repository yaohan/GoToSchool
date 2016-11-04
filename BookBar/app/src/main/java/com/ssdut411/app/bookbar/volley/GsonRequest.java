package com.ssdut411.app.bookbar.volley;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;
import com.ssdut411.app.bookbar.model.Book;
import com.ssdut411.app.bookbar.utils.GsonUtils;
import com.ssdut411.app.bookbar.utils.L;

import java.util.Map;

/**
 * Created by yao_han on 2015/11/24.
 */
public class GsonRequest<T> extends Request<T> {
    /**
     * Default charset for JSON request.
     */
    protected static final String PROTOCOL_CHARSET = "utf-8";

    /**
     * 要转换的对象
     */
    private final Class<T> mClazz;

    /**
     * 请求回调监听器
     */
    private Response.Listener<T> mListener;

    /**
     * 请求参数
     */
    private Map<String, String> requestParams;

    public GsonRequest(int method, String url, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(method, url, null, clazz, listener, errorListener);
    }

    public GsonRequest(int method, String url, Map<String, String> params, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mClazz = clazz;
        mListener = listener;
        requestParams = params;
    }

//    @Override
//    public Map<String, String> getHeaders() throws AuthFailureError {
//        Map<String, String> mHeaders = HttpRequestRequest.getRequestHeaders();
//        return mHeaders != null ? mHeaders : super.getHeaders();
//    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return requestParams;
    }

    @Override
    protected void deliverResponse(T response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }

//    @Override
//    protected void onFinish() {
//        super.onFinish();
//        mListener = null;
//    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            // 得到返回的数据
            String json = new String(response.data);

            Log.i("GsonRequest", "服务器返回的json串：" + json);
            L.i("mClass:"+mClazz);
            L.i("parse:"+GsonUtils.gsonToObject(json,mClazz));

            // 转换为对象
            return Response.success(GsonUtils.gsonToObject(json, mClazz), HttpHeaderParser.parseCacheHeaders(response));
        }catch (JsonSyntaxException e) {
            L.i("JsonSyntaxException");
            return Response.error(new ParseError(e));
        }
    }
}
