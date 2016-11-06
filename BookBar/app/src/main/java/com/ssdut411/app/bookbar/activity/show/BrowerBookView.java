package com.ssdut411.app.bookbar.activity.show;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ssdut411.app.bookbar.utils.L;

/**
 * Created by LENOVO on 2016/10/23.
 */
public class BrowerBookView extends View
{
    float x,y,r=10;
    private boolean drawCircle;
    private Path path;
    public Paint paint=null;
    public Paint pointPaint=null;
    public Paint whitePaint=null;
    float startX;// 起始X
    float startY;// 起始Y
    float stopX;// 结束X
    float stopY;// 结束Y
    float stepLength=25;
    boolean isPause=true;
    float[][]points=new float[500][2];
    int pointsIndex=0;

    public void setStepLength(int sl)
    {
        stepLength=25;
    }
    public BrowerBookView(Context context)
    {
        super(context);
        path=new Path();
        paint=new Paint(Paint.DITHER_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        paint.setDither(true);

        pointPaint=new Paint(Paint.DITHER_FLAG);
        pointPaint.setColor(Color.RED);
        pointPaint.setStyle(Paint.Style.STROKE);
        pointPaint.setStrokeCap(Paint.Cap.ROUND);
        pointPaint.setStrokeWidth(40);
        pointPaint.setAntiAlias(true);
        pointPaint.setDither(true);

        whitePaint=new Paint(Paint.DITHER_FLAG);
        whitePaint.setColor(Color.WHITE);
        whitePaint.setStyle(Paint.Style.STROKE);
        whitePaint.setStrokeCap(Paint.Cap.ROUND);
        whitePaint.setStrokeWidth(40);
        whitePaint.setAntiAlias(true);
        whitePaint.setDither(true);
    }

    public BrowerBookView(Context context,AttributeSet attrs)
    {
        this(context);
    }


    public String pause()// 暂停/开始
    {
        if(isPause)
        {
            isPause=false;
            return "暂停";
        }
        isPause=true;
        return "开始";
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        drawPath(canvas);
//        if(drawCircle){
           canvas.drawCircle(x,y,r,paint);
//            drawCircle = false;
//        }
    }

    void drawPath(Canvas canvas){
        L.i("pointsIndex:"+pointsIndex);
        for(int i=0;i<pointsIndex-1;i++){
            canvas.drawLine(points[i][0], points[i][1], points[i+1][0], points[i+1][1], paint);
        }
    }

    public void move()
    {
        Log.i("yao", "move");
            float a=stopX-startX;
            float b=stopY-startY;
            float dx=(float)(a*stepLength/Math.sqrt(a*a+b*b));
            float dy=(float)(b*stepLength/Math.sqrt(a*a+b*b));
            startX=startX+dx;
            stopX=stopX+dx;
            startY=startY+dy;
            stopY=stopY+dy;
            points[pointsIndex][0]=startX;
            points[pointsIndex][1]=startY;
            pointsIndex++;
            invalidate();
    }

    public void setHeading(float ori){
            float sina=(float)Math.sin(ori);
            float cosa=(float)Math.cos(ori);
            float newStopX,newStopY;
            float length = (float) Math.sqrt(startX*startY+stopX+stopY)/6;
            newStopX=startX+length*cosa;
            newStopY=startY+length*sina;
            stopX=newStopX;
            stopY=newStopY;
            invalidate();
    }

    public void turn(float angularSpeed)
    {
        if(!isPause)
        {
            float angle=-angularSpeed*0.025f;
            float sina=(float)Math.sin(angle);
            float cosa=(float)Math.cos(angle);
            float newStopX,newStopY;
            newStopX=cosa*(stopX-startX)-(stopY-startY)*sina+startX;
            newStopY=sina*(stopX-startX)+(stopY-startY)*cosa+startY;
            stopX=newStopX;
            stopY=newStopY;
            invalidate();
        }
    }

    public void setStart(float x,float y){
        startX = x;
        startY = y;
        stopX = x;
        stopY = y-1;
    }

    public void drawCircle(float x,float y){
        this.x = x;
        this.y = y;
        drawCircle = true;
        invalidate();
    }
}
