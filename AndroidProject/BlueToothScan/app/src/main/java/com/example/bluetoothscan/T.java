package com.example.bluetoothscan;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast统一管理工具
 */
public class T {
    private T() {
        throw new UnsupportedOperationException("Toast Util can't be instantiated");
    }

    public static boolean isShow = true;

    public static Toast toast = null;
    public static void showShort(Context context, String msg){
        if(isShow){
            if(toast == null){
                toast = Toast.makeText(context,msg, Toast.LENGTH_SHORT);
            }else{
                toast.setText(msg);
            }
            toast.show();
        }
    }
}

