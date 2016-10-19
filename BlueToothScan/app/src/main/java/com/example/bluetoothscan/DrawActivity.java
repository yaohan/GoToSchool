package com.example.bluetoothscan;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

/**
 * Created by yao_han on 2016/10/9.
 */
public class DrawActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        RelativeLayout layout=(RelativeLayout) findViewById(R.id.rl_layout);
        final DrawView view=new DrawView(this);
        //通知view组件重绘
        Log.i("yao","before invalidate");
        view.invalidate();
        Log.i("yao", "after invalidate");
//        view.drawCircle(60, 350, 20);
//        view.drawCircle(1020, 350, 20);
//        view.drawCircle(60, 1350, 20);
//        view.drawCircle(1020, 1350, 20);
        layout.addView(view);
        Log.i("yao", "after addView");

    }
}
