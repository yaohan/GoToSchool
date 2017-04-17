package com.example.bluetoothscan.track;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluetoothscan.R;

/**
 * Created by yao_han on 2016/10/11.
 */
public class MainActivity extends Activity implements SensorEventListener,
        SeekBar.OnSeekBarChangeListener
{
    private Attitude attitude;
    SensorManager sensorManager=null;
    ActionBar actionBar;
    MapView map;
    int[] images=new int[]
            {R.drawable.blank,R.drawable.louceng1,R.drawable.example,R.drawable.example2};
    int currentImg=1;

    TextView ori =null;
    TextView ori1 =null;

    float[] values;
    float a0,a1,a2;// 三个方向上的加速度（加速度传感器）
    float g0,g1,g2;// 重力在三个方向上的分量（重力传感器）
    float v0,v1,v2;// 三个轴上的角速度（陀螺仪）
    float g02,g12,g22;// g0,g1,g2的平方
    float a02,a12,a22;// a0,a1,a2的平方
    float vertical;// 竖直方向加速度
    final float G2=96;
    final float g=(float)9.8;
    int time;
    int step;// 步数
    boolean flag=false;
    float threshold1,threshold2;// 加速度阈值
    float horizontalAngle=0;// 水平方向转角
    private long exitTime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        attitude = new Attitude(this);
        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
//		actionBar=getActionBar();
//		actionBar.hide();
//		final Button btnpause=(Button)findViewById(R.id.pause);
//		Button next=(Button)findViewById(R.id.next);
//		Button recountStepButton=(Button)findViewById(R.id.recount);
//		ori=(TextView) findViewById(R.id.ori);
//		ori1=(TextView) findViewById(R.id.ori2);
//		final Button pause=(Button)findViewById(R.id.pause);
//		SeekBar setStepLength=(SeekBar)findViewById(R.id.sbSetStepLength);
//		setStepLength.setMax(9);
//		setStepLength.setOnSeekBarChangeListener(this);
        map=(MapView)findViewById(R.id.map);
        attitude.mv=map;
        attitude.txt1=ori;
        attitude.txt2=ori1;
        map.setImageResource(images[0]);
//		next.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				map.setImageResource(images[++currentImg%images.length]);
//				map.clear();
//			}
//		});
//		pause.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				pause.setText(map.pause());
//			}
//		});
//		recountStepButton.setOnClickListener(new OnClickListener()
//		{
//
//			@Override
//			public void onClick(View v)
//			{
//				step=0;
//				threshold1=12;
//				threshold2=10;
//				horizontalAngle=0;
//				map.clear();
//			}
//		});
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event)
    {
        if(keyCode==KeyEvent.KEYCODE_BACK
                &&event.getAction()==KeyEvent.ACTION_DOWN)
        {
            if((System.currentTimeMillis()-exitTime)>2000)
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime=System.currentTimeMillis();
            }else
            {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
    @Override
    protected void onStart() {
        super.onStart();
        attitude.start();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_GAME);
//		sensorManager.registerListener(this,
//				sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
//				SensorManager.SENSOR_DELAY_GAME);

    }


    public void onPause()
    {
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onAccuracyChanged(Sensor arg0,int arg1)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        values=event.values;
        if(event.sensor.getType()==Sensor.TYPE_GRAVITY)
        {
            g0=(float)(Math.round((values[0])*100))/100;
            g1=(float)(Math.round((values[1])*100))/100;
            g2=(float)(Math.round((values[2])*100))/100;
        }
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
        {
            time=time+1;
            a0=(float)(Math.round((values[0])*100))/100;// 保留3位小数
            a1=(float)(Math.round((values[1])*100))/100;
            a2=(float)(Math.round((values[2])*100))/100;
            g02=(float)g0*g0;
            g12=(float)g1*g1;
            g22=(float)g2*g2;
            a12=(float)a1*a1;
            a22=(float)a2*a2;
            a02=(float)a0*a0;
            vertical=a0*(g02+G2-g12-g22)/(2*g0*g)+a1*(g12+G2-g02-g22)/(2*g1*g)
                    +a2*(g22+G2-g02-g12)/(2*g*g2);
            vertical=(float)(Math.round((vertical)*100))/100;
            if(vertical!=2.1474836e7&&vertical!=-2.1474836e7&&vertical!=0)
            {
                if(a2>0)
                {
                    threshold1=12;
                    threshold2=10;
                }else
                {
                    threshold1=10;
                    threshold2=8;
                }
                if(flag)
                {
                    if(vertical>threshold1&&time>=12)
                    {
                        step=step+1;// 走了一步
                        map.move();
                        time=0;
                        flag=!flag;
                    }
                }else
                {
                    if(vertical<threshold2)
                    {
                        flag=!flag;
                    }
                }
            }
        }
//		if(event.sensor.getType()==Sensor.TYPE_GYROSCOPE)
//		{
//			float gi;
//			float vi;
//			v0=(float)(Math.round((values[0])*100))/100;
//			v1=(float)(Math.round((values[1])*100))/100;
//			v2=(float)(Math.round((values[2])*100))/100;
//			if(g0>g1)
//			{
//				gi=g0;
//				vi=v0;
//			}else
//			{
//				gi=g1;
//				vi=v1;
//			}
//			if(g2>gi)
//			{
//				gi=g2;
//				vi=v2;
//			}
//			if(vi<0)
//			{
//				horizontalAngle=horizontalAngle
//						-(float)Math
//								.acos(1-(G2*(1-Math.cos(vi*0.025))/(gi*gi)));
//				vi=-(float)Math.acos(1-(G2*(1-Math.cos(vi))/(gi*gi)));
//			}else
//			{
//				horizontalAngle=horizontalAngle
//						+(float)Math
//								.acos(1-(G2*(1-Math.cos(vi*0.025))/(gi*gi)));
//				vi=(float)Math.acos(1-(G2*(1-Math.cos(vi))/(gi*gi)));
//			}
//			horizontalAngle=(float)(Math.round(horizontalAngle*100))/100;
//			if(!Float.isNaN(vi))
//			{
////				map.turn(vi);
//			}
//		}
    }

    public void onStop()
    {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onProgressChanged(SeekBar arg0,int progress,boolean arg2)
    {
        map.setStepLength(progress+5);
    }

    @Override
    public void onStartTrackingTouch(SeekBar arg0)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar arg0)
    {
        // TODO Auto-generated method stub

    }
}
