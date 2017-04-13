package com.ssdut411.app.bookbarstatic.activity.show;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


import com.ssdut411.app.bookbarstatic.R;

import java.util.List;

/**
 * Created by LENOVO on 2016/10/23.
 */
public class BrowerBookView extends View
{
    private float bookX;
    private float bookY;
    private boolean train=false;//测试阶段定位
    private boolean navigation=false;//导航阶段-路线
    private boolean location=false;//导航阶段-位置
    private boolean book=false;//导航阶段-图书
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
    float stepLength=25*3;
    boolean isPause=true;
    float[][]points=new float[500][2];
    int pointsIndex=0;
    private Context context;

    public void setStepLength(int sl)
    {
        stepLength=25;
    }
    public BrowerBookView(Context context)
    {
        super(context);
        this.context = context;
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
        if(navigation){
            drawPath(canvas);
        }
        if(location){
            canvas.drawCircle(x,y,r,paint);
        }
        if(book){
            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.aim_book),bookX-10,bookY-10,paint);
        }
        canvas.drawLine(startX,startY,stopX,stopY,paint);
        paint.setColor(Color.BLACK);
        canvas.drawCircle(startX, startY, 5, paint);
        paint.setColor(Color.RED);
        plotMap(canvas);
    }

    void drawPath(Canvas canvas){
//        L.i("pointsIndex:" + pointsIndex);
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
        ori = (float)(ori-Math.PI/2.0f);
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
        stopX = x+1;
        stopY = y;
        points[pointsIndex][0]=x;
        points[pointsIndex][1]=y;
        pointsIndex++;
    }

    public void drawCircle(float x,float y){
        this.x = x;
        this.y = y;
        drawCircle = true;
        invalidate();
    }
    @Override
    public boolean onTouchEvent(final MotionEvent event)
    {
        final float x=event.getX();
        final float y=event.getY();
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                if(train){
                    AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle("定位").setMessage("确定在此定位("+x+","+y+")").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CreateDBActivity createDBActivity = (CreateDBActivity) getContext();
                            Location location = new Location(x, y, createDBActivity.getWifiInfoList());
                            Log.i("yao", "put x:" + x + " y:" + y);

                        DBFile.getInstance().addLocation(location);
                        }
                    }).create();
                    dialog.show();
                    TextView textView = (TextView)dialog.findViewById(android.R.id.message);
                    textView.setTextColor(getResources().getColor(R.color.font_black));
                }
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

    public boolean isTrain() {
        return train;
    }

    public void setTrain(boolean train) {
        this.train = train;
    }

    public boolean isNavigation() {
        return navigation;
    }

    public void setNavigation(boolean navigation) {
        this.navigation = navigation;
    }

    public boolean isLocation() {
        return location;
    }

    public void setLocation(boolean location) {
        this.location = location;
    }

    public boolean isBook() {
        return book;
    }

    public void setBook(boolean book) {
        this.book = book;
    }

    public void printBook(float x, float y){
        bookX = x;
        bookY = y;
    }
    private void plotMap(Canvas canvas){
        paint.setColor(Color.BLACK);
        canvas.drawRect(100, 470, 980, 1450, paint);
        canvas.drawRect(130, 590, 770, 710, paint);
        canvas.drawRect(280,890,820,1020,paint);
        canvas.drawRect(220,1200,820,1320,paint);
        paint.setColor(Color.RED);
    }
}
