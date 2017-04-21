package com.example.bluetoothscan;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * 传感器信息详情页，包含传感器的名字，供应商，精度，具体数值信息
 */
public class DetailActivity extends Activity implements SensorEventListener {

    protected SensorManager sensorManager;
    private TextView textView;
    private DrawView drawView;
    private Attitude attitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
    }

    private void initView() {
        //控件绑定
        textView = (TextView)findViewById(R.id.detail);
        //获取传感器service
        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);

        drawView = new DrawView(this);
        RelativeLayout linearLayout = (RelativeLayout)findViewById(R.id.ll_layout);
        linearLayout.addView(drawView);

        getActionBar().setDisplayHomeAsUpEnabled(false);
        setTitle("传感器详情");

        attitude = new Attitude(drawView,textView);
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        //页面创建时执行，在线这里进行注册
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        //页面销毁时执行，在这里进行反注册
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //传感器信息改变时执行
        attitude.calcSensorEvent(sensorEvent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //传感器精度改变时执行
    }

}
