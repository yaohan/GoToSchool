package com.ssdut411.myapplication;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener{

    SensorManager sensorManager=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
    }
    public void onPause()
    {
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        TextView text02=(TextView)findViewById(R.id.textView1);
        TextView text12=(TextView)findViewById(R.id.textView2);
        TextView text22=(TextView)findViewById(R.id.textView3);
        text02.setText(""+event.values[0]);
        text12.setText(""+event.values[1]);
        text22.setText("" + event.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub

    }

}
