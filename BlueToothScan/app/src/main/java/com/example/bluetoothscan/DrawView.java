package com.example.bluetoothscan;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yao_han on 2016/10/9.
 */
public class DrawView extends View {
    private Paint paint;
    private List<Float> pathX, pathY;
    private int width, height, centerX, beforeY, roomSize = 5;
    private float length = (float)0.25;
    private float nowX, nowY;
    private float attitude;

    public DrawView(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.RED);// 设置红色
        paint.setStrokeWidth(10);
        paint.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了
        pathX = new ArrayList<Float>();
        pathY = new ArrayList<Float>();
        Rect outRect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        int contentTop = ((Activity) context).getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        width = outRect.width();
        height = outRect.height() - 192;
        Log.i("yao", "width:" + width + "  height:" + height + "top" + outRect.top + "  contentTop" + contentTop);
        centerX = width / 2;
        beforeY = (height - width) / 2;
        move(0,0);
    }

    public DrawView(Context context, AttributeSet attrs) {
        this(context);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context);
    }



    public void setAttitude(float ori) {
        Log.i("yao","attitude:"+ori);
        attitude = ori;
    }

    public void move() {
        float x = nowX + length * (float) Math.cos(attitude);
        float y = nowY + length * (float) Math.sin(attitude);
        move(x, y);
        nowX = x;
        nowY = y;
    }

    public void move(float x, float y) {
        Log.i("yao","move to "+x+"   "+y);
        if (Math.abs(x) <= 5 && Math.abs(y) <= 5) {
            float realX = (x + 5) / 10 * width;
            float realY = beforeY + (y + 5) / 10 * width;
            pathX.add(realX);
            pathY.add(realY);
            if (pathX.size() > 1) {
                Log.i("yao","invalidate");
                invalidate();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("yao","onDraw");
        super.onDraw(canvas);
        drawPath(canvas);
    }

    private void drawPath(Canvas canvas) {
        for (int i = 0; i < pathX.size() - 1; i++) {
            canvas.drawLine(pathX.get(i), pathY.get(i), pathX.get(i + 1), pathY.get(i + 1), paint);
        }
    }
}
