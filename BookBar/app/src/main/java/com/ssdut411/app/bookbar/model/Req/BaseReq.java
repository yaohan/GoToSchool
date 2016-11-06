package com.ssdut411.app.bookbar.model.Req;

import android.util.Log;

import com.ssdut411.app.bookbar.utils.L;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;

/**
 * Created by yao_han on 2015/12/23.
 */
public class BaseReq {

    public BaseReq() {
    }

    /**
     * 判断当前类的属性是否有null值
     * @return 返回值为null的属性名，null表示没有空值。
     */
    public String hasNull(){
        try{
            for(Field f : getClass().getDeclaredFields()){
                f.setAccessible(true);
                    if(f.get(this) == null){
                        return f.getName();
                    }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //拼接get参数的
    public String toGetFormat(){
        StringBuffer sb =  new StringBuffer();
        sb.append("?");
        boolean flag = true;
        try{
            for(Field f:getClass().getDeclaredFields()){
//                Log.i("Field-----", f.toString() + f.getName());
                f.setAccessible(true);
                if(flag){
                    flag = false;
                }else{
                    sb.append("&");
                }
                sb.append(f.getName());//属性名
                sb.append("=");
                if(f.get(this) == null){
                    sb.append("");//属性值
                }else{
                    String value;
                    try {
                        value = URLEncoder.encode(f.get(this).toString(), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        value = "";
                    }
                    sb.append(value);//属性值
                }
            }}catch (Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }
}
