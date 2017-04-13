package com.ssdut411.app.bookbarstatic.activity.show;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;


import com.ssdut411.app.bookbarstatic.R;
import com.ssdut411.app.bookbarstatic.activity.mainPage.CaptureActivity;
import com.ssdut411.app.bookbarstatic.activity.system.BaseActivity;
import com.ssdut411.app.bookbarstatic.activity.system.MainApplication;
import com.ssdut411.app.bookbarstatic.utils.L;
import com.ssdut411.app.bookbarstatic.utils.T;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by LENOVO on 2016/10/23.
 */
public class FindBookActivity extends BaseActivity implements SensorEventListener {
    private List<WifiInfo> wifiInfoList;
    private Timer timer = new Timer();
    private BrowerBookView browerBookView;
    private Location nowLocation;
    private boolean start = true;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (nowLocation != null) {
//                T.showShort(context,"x:"+nowLocation.getX()+" y:"+nowLocation.getY());
                browerBookView.drawCircle(nowLocation.getX(), nowLocation.getY());
                Log.i("yao", "get x:" + nowLocation.getX() + " y:" + nowLocation.getY());
                if(start){
                    browerBookView.setStart(nowLocation.getX(),nowLocation.getY());
                    start = false;
                }
            }
        }
    };

    private Attitude attitude;
    SensorManager sensorManager=null;
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
    protected String initTitle() {
        return "图书导航";
    }

    @Override
    protected int initMenu() {
        return 0;
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_find_book;
    }

    @Override
    protected void initViews() {
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.layout);
        browerBookView = new BrowerBookView(FindBookActivity.this);
        browerBookView.setLocation(true);
        browerBookView.setNavigation(true);
        browerBookView.setBook(true);
        layout.addView(browerBookView);
        startScanWIFI();
        attitude = new Attitude(this);
        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        attitude.mv=browerBookView;
        getButton(R.id.bt_find_borrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainApplication.getInstance().setDirectBorrow(true);
                startActivity(new Intent(context, CaptureActivity.class));
            }
        });
        getButton(R.id.tv_find_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                T.showShort(context, "onClick");
                startActivity(new Intent(context, CreateDBActivity.class));
            }
        });
    }

    @Override
    protected void loadData() {
        float x = getIntent().getFloatExtra("locationX",540);
        float y = getIntent().getFloatExtra("locationY",960);
        L.i("x:" + x + "y:" + y);
        browerBookView.printBook(x,y);
    }

    @Override
    protected void showView() {
        setCanBack();
    }

    private void startScanWIFI() {
        final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        wifiManager.startScan();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                List<ScanResult> list = wifiManager.getScanResults();
                List<WifiInfo> wifiList = new ArrayList<WifiInfo>();
                for (int i = 0; i < list.size(); i++) {
                    WifiInfo wifiInfo = new WifiInfo(list.get(i).BSSID, WifiManager.calculateSignalLevel(list.get(i).level, 100));
                    wifiList.add(wifiInfo);
                }
                wifiInfoList = wifiList;
                Location location = DBFile.getInstance().searchLocation(wifiInfoList);
                nowLocation = location;
                Message msg = new Message();
                handler.sendMessage(msg);
            }
        }, 1000, 1000);
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
    }
    public void onPause()
    {
        sensorManager.unregisterListener(this);
        timer.cancel();
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
                        browerBookView.move();
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
        }}

    public void onStop()
    {
        sensorManager.unregisterListener(this);

        super.onStop();
    }
}