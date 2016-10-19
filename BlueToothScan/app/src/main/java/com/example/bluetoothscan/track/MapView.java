package com.example.bluetoothscan.track;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

class MapView extends ImageView
{
	private Path path;
	public Paint paint=null;
	public Paint pointPaint=null;
	public Paint comparedPaint=null;
	public Paint whitePaint=null;
	int VIEW_WIDTH=1000;
	int VIEW_HEIGHT=1000;
	Bitmap cacheBitmap=null;
	Canvas cacheCanvas=null;
	float preX;
	float preY;
	float startX;// 起始X
	float startY;// 起始Y
	float compassStartX;// 结束X
	float compassStartY;// 结束Y
	float stopX;// 结束X
	float stopY;// 结束Y
	float compassStopX;// 结束X
	float compassStopY;// 结束Y
	Paint bmpPaint=new Paint();
	boolean isInitialised=false;
	boolean isClear=false;
	float angle;
	float stepLength=25;
	boolean isPause=true;
	float[][]points=new float[500][2];
	float[][]comparedPoints=new float[500][2];
	int pointsIndex=0;

	public void setStepLength(int sl)
	{
		stepLength=25;
	}
	public MapView(Context context)
	{
		super(context);
	}

	public MapView(Context context,AttributeSet attrs)
	{
		super(context,attrs);
		cacheBitmap=Bitmap.createBitmap(1000,1000, Bitmap.Config.ARGB_8888);
		cacheCanvas=new Canvas();
		path=new Path();
		cacheCanvas.setBitmap(cacheBitmap);
		paint=new Paint(Paint.DITHER_FLAG);
		paint.setColor(Color.RED);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(5);
		paint.setAntiAlias(true);
		paint.setDither(true);
		comparedPaint=new Paint(Paint.DITHER_FLAG);
		comparedPaint.setColor(Color.BLUE);
		comparedPaint.setStyle(Paint.Style.STROKE);
		comparedPaint.setStrokeWidth(5);
		comparedPaint.setAntiAlias(true);
		comparedPaint.setDither(true);

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

	public void clear()
	{
		isClear=true;
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
		if(isClear)
		{
			canvas=new Canvas();
			cacheBitmap=Bitmap.createBitmap(1000,1000, Bitmap.Config.ARGB_8888);
			cacheCanvas.setBitmap(cacheBitmap);
			isClear=false;
			stopX=startX;
			stopY=startY;
			pointPaint.setStrokeWidth(0);
		}else
		{
			// canvas.drawBitmap(cacheBitmap,0,0,bmpPaint);
//			drawAL(startX,startY,stopX,stopY,canvas,paint);
//			drawAL(compassStartX,compassStartY,compassStopX,compassStopY,canvas,comparedPaint);
			drawPath(canvas);
		}
	}

	void drawPath(Canvas canvas){
		for(int i=0;i<pointsIndex-1;i++){
			canvas.drawLine(points[i][0], points[i][1], points[i+1][0], points[i+1][1], paint);
//			canvas.drawLine(comparedPoints[i][0], comparedPoints[i][1], comparedPoints[i+1][0], comparedPoints[i+1][1], comparedPaint);
		}
	}

	// 画箭头，表示方向
	void drawAL(float startX,float startY,float stopX,float stopY,Canvas canvas,Paint paint)
	{
		float p1x,p1y;// 箭头两边的点1
		float p2x,p2y;// 箭头两边的点2
		float x=startX+0.75f*(stopX-startX),y=startY+0.75f*(stopY-startY);// 绕stop点旋转的点
		p1x=0.7f*(x-stopX-y+stopY)+stopX;
		p1y=0.7f*(x-stopX+y-stopY)+stopY;
		p2x=0.7f*(x-stopX+y-stopY)+stopX;
		p2y=0.7f*(stopX-x+y-stopY)+stopY;
		canvas.drawLine(startX,startY,stopX,stopY,paint);
		canvas.drawLine(stopX,stopY,p1x,p1y,paint);
		canvas.drawLine(stopX,stopY,p2x,p2y,paint);
		canvas.drawPoint(startX,startY,pointPaint);
	}

	public void move()
	{
		if(!isPause)
		{
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

			a=compassStopX-compassStartX;
			b=compassStopY-compassStartY;
			dx=(float)(a*stepLength/Math.sqrt(a*a+b*b));
			dy=(float)(b*stepLength/Math.sqrt(a*a+b*b));
			compassStartX=compassStartX+dx;
			compassStopX=compassStopX+dx;
			compassStartY=compassStartY+dy;
			compassStopY=compassStopY+dy;
			comparedPoints[pointsIndex][0]=compassStartX;
			comparedPoints[pointsIndex][1]=compassStartY;
			pointsIndex++;
			invalidate();
		}
	}

	public void setHeading(float ori){
		if(!isPause){
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
	}

	public void setCompassHeading(float ori){
		if(!isPause){
			float sina=(float)Math.sin(ori);
			float cosa=(float)Math.cos(ori);
			float newStopX,newStopY;
			float length = (float) Math.sqrt(compassStartX*compassStartY+compassStopX+compassStopY)/4;
			newStopX=compassStartX+length*cosa;
			newStopY=compassStartY+length*sina;
			compassStopX=newStopX;
			compassStopY=newStopY;
			invalidate();
		}
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

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		float x=event.getX();
		float y=event.getY();
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				startX=x;
				startY=y;
				stopX=x;
				stopY=y-1;
				pointPaint.setStrokeWidth(10);
				compassStartX=x;
				compassStartY=y;
				compassStopX=x;
				compassStopY=y;
				break;
			case MotionEvent.ACTION_MOVE:
				stopX=x;
				stopY=y;
				compassStopX=x;
				compassStopY=y;
				break;
			case MotionEvent.ACTION_UP:
				pointsIndex=0;
				isPause=false;
//				isPause=true;
//				Timer timer=new Timer();
//				timer.schedule(new TimerTask()
//				{
//					@Override
//					public void run()
//					{
//						isPause=false;
//					}
//				},1500);
				break;
			default:
				break;
		}
		invalidate();
		return true;
	}
}