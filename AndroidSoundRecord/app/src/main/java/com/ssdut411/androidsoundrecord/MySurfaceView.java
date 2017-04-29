package com.ssdut411.androidsoundrecord;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by yao_han on 2017/4/28.
 */
public class MySurfaceView extends SurfaceView implements Runnable, SurfaceHolder.Callback {


    private SurfaceHolder mHolder;    //用于控制SurfaceView
    private Canvas mCanvas;    //声明一张画布
    private Paint mPaint, qPaint;    //声明两只画笔
    private Path mPath, qPath, tPath;    //声明三条路径
    private int mX, mY;    //用于控制图形的坐标
    //分别 代表贝塞尔曲线的开始坐标,结束坐标,控制点坐标
    private int qStartX, qStartY, qEndX, qEndY, qControlX, qCOntrolY;
    private int screenW, screenH;    //用于屏幕的宽高
    private Thread mThread;    //声明一个线程
    //flag用于线程的标识,xReturn用于标识图形坐标是否返回,cReturn用于标识贝塞尔曲线的控制点坐标是否返回
    private boolean flag, xReturn, cReturn;
    private Bitmap mBmpFace;


    final short LEAD_SIZE = 1;
    float[][] cy = new float[LEAD_SIZE][10];
    float[] mWavePoint;
    int nScreenWidth = 0;
    int nScreenHeight = 0;
    int nUnitHeight = 0;
    int x = 100;
    int cx = 5;
    boolean bColor = true;

    /**
     * 构造函数，主要对一些对象初始化
     */
    public MySurfaceView(Context context) {
        this(context, null);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }
    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mHolder = this.getHolder();    //获得SurfaceHolder对象
        mHolder.addCallback(this);    //添加状态监听


        mPaint = new Paint();        //创建一个画笔对象
        mPaint.setColor(Color.WHITE);   //设置画笔的颜色为白色


        qPaint = new Paint();        //创建一个画笔对象
        qPaint.setAntiAlias(true);   //消除锯齿
        qPaint.setStyle(Paint.Style.STROKE);  //设置画笔风格为描边
        qPaint.setStrokeWidth(3);        //设置描边的宽度为3
        qPaint.setColor(Color.GREEN);   //设置画笔的颜色为绿色


        //创建路径对象
        mPath = new Path();
        qPath = new Path();
        tPath = new Path();


        //设置坐标为50,100
        mX = 50;
        mY = 100;


        //设置贝塞尔曲线的开始坐标为(10,200)
        qStartX = 10;
        qStartY = 200;


        setFocusable(true);    //设置焦点
    }


    /**
     * 当SurfaceView创建的时候调用
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        //获得屏幕的宽和高
        screenW = this.getWidth();
        screenH = this.getHeight();
        nUnitHeight = screenH / (LEAD_SIZE + 2);
        mBmpFace = Bitmap.createBitmap(screenW, screenH, Bitmap.Config.RGB_565);
        mWavePoint = new float[LEAD_SIZE * 4 * 4];
        cy[0][0] = 5;
//        cy[0][0] = cy[1][0] = cy[2][0] = 5;
        cy[0][1] = nUnitHeight - (int) (20 * Math.sin((0) * 2 * Math.PI / nUnitHeight));
//        cy[1][1] = nUnitHeight - (int) (20 * Math.sin((0) * 2 * Math.PI / nUnitHeight));
//        cy[2][1] = nUnitHeight - (int) (10 * Math.sin((0) * 2 * Math.PI / nUnitHeight));


        qEndX = screenW - 10;   //设置贝塞尔曲线的终点横坐标为屏幕宽度减10
        qEndY = qStartY;        //设置贝塞尔曲线的终点纵坐标和起点纵坐标一样

        //设置贝塞尔曲线的控制点坐标为终点坐标与起点坐标对应的差值的一半,注意这里不是曲线的中点
        qControlX = (qEndX - qStartX) / 2;
        qCOntrolY = (qEndY - qStartY) / 2;


        DrawMethod();

        mThread = new Thread(this);    //创建线程对象
        flag = true;        //设置线程标识为true
        xReturn = false;    //设置图形坐标不返回
        cReturn = false;    //设置贝塞尔曲线控制点坐标不返回
        mThread.start();    //启动线程
    }


    /**
     * 当SurfaceView视图发生改变的时候调用
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }


    /**
     * 当SurfaceView销毁的时候调用
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        flag = false;    //设置线程的标识为false
    }


    /**
     * 线程运行的方法,当线程start后执行
     */
    @Override
    public void run() {


        while (flag) {
            canvasDrawPath();
            //canvasDrawLine();    	//调用自定义的绘图方法
            //mGameLogic();    //调用自定义的逻辑方法
            try {
                Thread.sleep(120);    //让线程休息50毫秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     *     自定义的绘图方法
     */


    /**
     * 自定义的方法,简单绘制一些基本图形
     *
     */
    public void canvasDrawLine() {
        mPaint.setStrokeWidth((float) 2.0);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

        int nCurHeight = nUnitHeight;
        for (int i = 0; i < LEAD_SIZE; i++) {
            nCurHeight = nUnitHeight * (1 + i);
            x = (int) (nUnitHeight * 0.6);
            cy[i][2] = cx + 1;
            cy[i][4] = cx + 2;
            cy[i][6] = cx + 3;
            cy[i][8] = cx + 4;
            cy[i][3] = nCurHeight - (int) (x * Math.sin((cy[i][2] - 5) * 2 * Math.PI / nUnitHeight));
            cy[i][5] = nCurHeight - (int) (x * Math.sin((cy[i][4] - 5) * 2 * Math.PI / nUnitHeight));
            cy[i][7] = nCurHeight - (int) (x * Math.sin((cy[i][6] - 5) * 2 * Math.PI / nUnitHeight));
            cy[i][9] = nCurHeight - (int) (x * Math.sin((cy[i][8] - 5) * 2 * Math.PI / nUnitHeight));

            //mPaint.setColor(Color.GRAY);//白色背景
            //mPaint.setDither(true);
            //canvas.drawLine(X_OFFSET, nCurHeight, WIDTH, nCurHeight, paint);

            // Line 1
            mWavePoint[i * 16 + 0] = cy[i][0];// 此点为多边形的起点
            mWavePoint[i * 16 + 1] = cy[i][1];// 此点为多边形的起点
            mWavePoint[i * 16 + 2] = cy[i][2];
            mWavePoint[i * 16 + 3] = cy[i][3];
//            // Line 2
            mWavePoint[i * 16 + 4] = cy[i][2];
            mWavePoint[i * 16 + 5] = cy[i][3];
            mWavePoint[i * 16 + 6] = cy[i][4];
            mWavePoint[i * 16 + 7] = cy[i][5];
            // Line 3
            mWavePoint[i * 16 + 8] = cy[i][4];
            mWavePoint[i * 16 + 9] = cy[i][5];
            mWavePoint[i * 16 + 10] = cy[i][6];
            mWavePoint[i * 16 + 11] = cy[i][7];
            // Line 4
            mWavePoint[i * 16 + 12] = cy[i][6];
            mWavePoint[i * 16 + 13] = cy[i][7];
            mWavePoint[i * 16 + 14] = cy[i][8];
            mWavePoint[i * 16 + 15] = cy[i][9];

            cy[i][0] = cy[i][8];
            cy[i][1] = cy[i][9];
        }
        Rect rect = new Rect(cx - 1, (int) cy[0][1] - 100, cx + 5, (int) cy[0][1] + 1000);
        mCanvas = mHolder.lockCanvas(rect);    //获得画布对象,开始对画布画画
        mCanvas.drawColor(Color.BLACK);    //设置画布颜色为黑色
        //mCanvas.drawColor(Color.WHITE);
        mCanvas.drawLines(mWavePoint, mPaint);
        mHolder.unlockCanvasAndPost(mCanvas);    //把画布显示在屏幕上
        cx += 5;    //cx 自增， 就类似于随时间轴的图形
        if (cx >= 800) {
            cx = 5;     //如果画满则从头开始画
            //drawBack(holder);  //画满之后，清除原来的图像，从新开始
            mPaint.setColor(bColor ? Color.BLUE : Color.RED);
            bColor = !bColor;
        }
    }

    public void canvasDrawPath() {
        mPaint.setStrokeWidth((float) 2.0);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

        Path path = new Path();
        int nCurHeight = nUnitHeight;
        Rect rect = new Rect(cx - 1, (int) cy[0][1] - 100, cx + 5, (int) cy[0][1] + 1000);
        mCanvas = mHolder.lockCanvas(rect);    //获得画布对象,开始对画布画画
        mCanvas.drawColor(Color.BLACK);    //设置画布颜色为黑色

        for (int i = 0; i < LEAD_SIZE; i++) {
            nCurHeight = nUnitHeight * (1 + i);
            x = (int) (nUnitHeight * 0.6);
            cy[i][2] = cx + 1;
            cy[i][4] = cx + 2;
            cy[i][6] = cx + 3;
            cy[i][8] = cx + 4;
            cy[i][3] = nCurHeight - (int) (x * Math.sin((cy[i][2] - 5) * 2 * Math.PI / nUnitHeight));
            cy[i][5] = nCurHeight - (int) (x * Math.sin((cy[i][4] - 5) * 2 * Math.PI / nUnitHeight));
            cy[i][7] = nCurHeight - (int) (x * Math.sin((cy[i][6] - 5) * 2 * Math.PI / nUnitHeight));
            cy[i][9] = nCurHeight - (int) (x * Math.sin((cy[i][8] - 5) * 2 * Math.PI / nUnitHeight));

            path.moveTo(cy[i][0], cy[i][1]);// 此点为多边形的起点
            path.lineTo(cy[i][2], cy[i][3]);
            path.lineTo(cy[i][4], cy[i][5]);
            path.lineTo(cy[i][6], cy[i][7]);
            path.lineTo(cy[i][8], cy[i][9]);

            //path.close(); // 使这些点构成封闭的多边形
            mCanvas.drawPath(path, mPaint);
            cy[i][0] = cy[i][8];
            cy[i][1] = cy[i][9];
        }


        mHolder.unlockCanvasAndPost(mCanvas);    //把画布显示在屏幕上
        cx += 5;    //cx 自增， 就类似于随时间轴的图形
        if (cx >= 800) {
            cx = 5;     //如果画满则从头开始画
            //drawBack(holder);  //画满之后，清除原来的图像，从新开始
            mPaint.setColor(bColor ? Color.BLUE : Color.RED);
            bColor = !bColor;
        }
    }

    public void DrawMethod() {
        mPaint.setColor(Color.GREEN);
        Canvas mCanvas = new Canvas(mBmpFace);
        //创建对应坐标的矩形区域
        RectF mArc = new RectF(mX, mY - 70, mX + 50, mY - 20);
        //画填充弧,在矩形区域内,从弧的最右边开始,画270度,然后再通过连接圆心来填充
        mCanvas.drawArc(mArc, 0, 270, true, mPaint);

        //获得icon的Bitmap对象
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        //画图片
        mCanvas.drawBitmap(mBitmap, mX, mY, mPaint);


        //画圆,(x轴,y轴,半径,画笔)
        mCanvas.drawCircle(mX + 10, mY + 60, 10, mPaint);


        //画一条线,(起点横坐标,起点纵坐标,终点横坐标,终点纵坐标,画笔)
        mCanvas.drawLine(mX, mY + 75, mX + 20, mY + 75, mPaint);
        //画多条线,(坐标数组,画笔)坐标数组里每四个值构成一条线
        mCanvas.drawLines(new float[]{
                mX + 50, mY + 45, mX + 50, mY + 75,
                mX + 60, mY + 45, mX + 60, mY + 75,
                mX + 70, mY + 45, mX + 70, mY + 75}, mPaint);


        //创建对应矩形区域
        RectF mOval = new RectF(mX, mY + 80, mX + 60, mY + 110);
        //画椭圆
        mCanvas.drawOval(mOval, mPaint);


        /*
         * Paint qPaint = new Paint(); qPaint.setColor(Color.RED);
         * mCanvas.drawPaint(qPaint);
*/


        //重置Path里的所有路径
        mPath.reset();
        //设置Path的起点
        mPath.moveTo(mX, mY + 120);
        //第二个点
        mPath.lineTo(screenW - 10, mY + 120);
        //第三个点
        mPath.lineTo(screenW - 10, mY + 150);
        mPath.lineTo(screenW - 120, mY + 180);
        //画出路径,这里画的是三角形
        mCanvas.drawPath(mPath, mPaint);


        //重置Path里的所有路径
        qPath.reset();
        //设置Path的起点
        qPath.moveTo(qStartX, qStartY);
        //设置贝塞尔曲线的控制点坐标和终点坐标
        qPath.quadTo(qControlX, qCOntrolY, qEndX / 2, qEndY / 2);
        qPath.quadTo(qEndX / 2, qEndY / 2, qEndX, qEndY);
        //画出贝塞尔曲线
        mCanvas.drawPath(qPath, qPaint);


        //画点
        mCanvas.drawPoint(mX, mY + 155, qPaint);
        //画多个点,坐标数组每两个值代表一个点的坐标
        mCanvas.drawPoints(new float[]{mX, mY + 160, mX + 5, mY + 160,
                mX + 5, mY + 160}, qPaint);


        //画矩形
        mCanvas.drawRect(mX, mY + 170, mX + 100, mY + 220, mPaint);


        //设置矩形区域
        RectF mRect = new RectF(mX, mY + 230, mX + 100, mY + 260);
        //画圆角矩形,这个方法的第二第三个参数在后面有图讲解
        mCanvas.drawRoundRect(mRect, 10, 10, mPaint);
        //画文本
        mCanvas.drawText("drawText", mX, mY + 290, mPaint);
        //画文本,数组里每两个值代表文本的一个字符的坐标，数组的坐标可以比字符串里的字符多，但不可以少
        mCanvas.drawPosText("哈哈你好", new float[]{mX, mY + 310, mX + 20,
                mY + 310, mX + 40, mY + 310, mX + 60, mY + 310}, mPaint);


        //重置Path
        tPath.reset();
        //添加一个圆形路径,坐标,半径,方向(顺时针还是逆时针)
        tPath.addCircle(mX + 10, mY + 340, 10, Path.Direction.CW);
        //画出路径
        mCanvas.drawPath(tPath, qPaint);
        //把文本画在路径上,但不会画出路径
        mCanvas.drawTextOnPath("draw", tPath, 30, 0, mPaint);

    }

    /**
     * 自定义游戏逻辑方法
     */
    public void mGameLogic() {

        //判断图形横坐标是否返回
        if (!xReturn) {    //横坐标不返回
            //判断图形横坐标是否小于屏幕宽度减去100
            if (mX < (screenW - 100)) {        //小于
                mX += 3;    //横坐标往右3
            } else {    //不小于
                xReturn = true;    //设置横坐标返回
            }
        } else {    //横坐标返回
            //判断横坐标是否大于10
            if (mX > 10) {    //大于
                mX -= 3;    //横坐标往左3
            } else {    //不大于
                xReturn = false;    //设置横坐标不返回
            }
        }


        //判断贝塞尔曲线的控制点横坐标是否返回
        if (!cReturn) {    //控制点横坐标不返回
            //判断控制点横坐标是否小于终点横坐标减3
            if (qControlX < (qEndX - 3)) {    //小于
                qControlX += 3;    //控制点横坐标往右3
            } else {    //不小于
                cReturn = true;    //设置控制点横坐标返回
            }
        } else {    //控制点横坐标返回
            //判断控制点横坐标是否大于起点横坐标加3
            if (qControlX > (qStartX + 3)) {    //大于
                qControlX -= 3;    //控制点横坐标减3
            } else {    //不大于
                cReturn = false;    //设置控制点横坐标不返回
            }
        }
    }


    /**
     * 当屏幕被触摸时调用
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //设置贝塞尔曲线的坐标为触摸时坐标
        qStartX = (int) event.getX();
        qStartY = (int) event.getY();

        //设置贝塞尔曲线的控制点坐标为终点坐标与起点坐标对应的差值的一半,注意这里不是曲线的中点
        qControlX = Math.abs(qEndX - qStartX) / 2;
        qCOntrolY = Math.abs(qEndY - qStartY) / 2;

        //设置控制点的横坐标不返回
        cReturn = false;
        return super.onTouchEvent(event);
    }
}
