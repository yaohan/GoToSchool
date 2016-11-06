package com.ssdut411.app.bookbar.activity.show;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ssdut411.app.bookbar.model.Req.UploadPrintReq;
import com.ssdut411.app.bookbar.model.Resp.UploadPrintResp;
import com.ssdut411.app.bookbar.utils.GsonUtils;
import com.ssdut411.app.bookbar.utils.L;
import com.ssdut411.app.bookbar.utils.T;
import com.ssdut411.app.bookbar.volley.core.ActionCallbackListener;
import com.ssdut411.app.bookbar.volley.core.AppAction;
import com.ssdut411.app.bookbar.volley.core.AppActionImpl;

import java.util.List;


/**
 * Created by LENOVO on 2016/10/23.
 */
public class CreateDBView extends View {
    Context context;
    public CreateDBView(Context context, AttributeSet attrs) {
        this(context);
    }

    public CreateDBView(Context context) {
        super(context);
        this.context = context;
    }

    public CreateDBView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context);
    }
    @Override
    public boolean onTouchEvent(final MotionEvent event)
    {
        final float x=event.getX();
        final float y=event.getY();
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                new AlertDialog.Builder(getContext()).setTitle("定位").setMessage("确定在此定位").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CreateDBActivity createDBActivity = (CreateDBActivity) getContext();
                        List<WifiInfo> wifiInfos = createDBActivity.getWifiInfoList();
                        Location location = new Location(x, y, createDBActivity.getWifiInfoList());
                        Log.i("yao", "put x:" + x + " y:" + y);
                        AppAction action = new AppActionImpl(context);
                        UploadPrintReq uploadPrintReq = new UploadPrintReq();
                        uploadPrintReq.setLocationX(x+"");
                        uploadPrintReq.setLocationY(y + "");
                        L.i("wifiInfos:"+GsonUtils.gsonToJsonString(wifiInfos));
                        uploadPrintReq.setWifiInfoList(GsonUtils.gsonToJsonString(wifiInfos));
                        action.uploadPrint(uploadPrintReq, new ActionCallbackListener<UploadPrintResp>() {
                            @Override
                            public void onSuccess(UploadPrintResp data) {
                                T.showShort(context, data.getDesc());
                            }

                            @Override
                            public void onFailure(String message) {
                                T.showShort(context,message);
                                L.i("message:"+message);
                            }
                        });
//                        DBFile.addLocation(location);
                    }
                }).create().show();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }
}
