package com.ssdut411.app.bookbar.activity.show;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by LENOVO on 2016/10/23.
 */
public class CreateDBView extends View {
    public CreateDBView(Context context, AttributeSet attrs) {
        this(context);
    }

    public CreateDBView(Context context) {
        super(context);
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
                        CreateDBActivity createDBActivity = (CreateDBActivity)getContext();
                        Location location = new Location(x,y,createDBActivity.getWifiInfoList());
                        Log.i("yao","put x:"+x+" y:"+y);
                        DBFile.addLocation(location);
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
