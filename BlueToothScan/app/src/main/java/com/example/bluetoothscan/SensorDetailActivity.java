package com.example.bluetoothscan;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by yao_han on 2016/10/11.
 */
public class SensorDetailActivity extends Activity implements SensorEventListener {

    private int type;
    private String string;
    protected SensorManager sensorManager;
    private TextView textView;
    private double sum[] = new double[3];
    private double time = 0;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_detail);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("传感器详情");
        //获取传感器service
        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        //控件绑定
        textView = (TextView)findViewById(R.id.detail);

        //从Intent获取上一个页面传递过来的数据，type表示传感器类型，string表示传感器信息
        type = getIntent().getIntExtra("type", 0);
        string = getIntent().getStringExtra("string");
        if(type == 0){
            T.showShort(this, "type is 0");
            finish();
        }
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        //页面创建时执行，在线这里进行注册
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(type),
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
        //传感器信息改变时执行，在这里将数据传递给文本框进行展示
        textView.setText(getInfo(sensorEvent));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //传感器精度改变时执行
    }

    //获取传感器信息
    private String getInfo(SensorEvent sensorEvent){
        StringBuffer sb = new StringBuffer(string);
        if(time == 0){
            time = sensorEvent.timestamp;
        }else{
            double duration = (sensorEvent.timestamp-time)/1000000000;
            time = sensorEvent.timestamp;
//            sum[0]+=sensorEvent.values[0]*duration;
//            sum[1]+=sensorEvent.values[1]*duration;
//            sum[2]+=sensorEvent.values[2]*duration;
        }
        sum[0]=sensorEvent.values[0];
        sum[1]=sensorEvent.values[1];
        sum[2]=sensorEvent.values[2];

        time++;
        sb.append("精度："+sensorEvent.accuracy+"\n");
        sb.append("x:"+sum[0]+"  y:"+sum[1]+"  z:"+sum[2]);
        return sb.toString();
    }

    //将数组转换成字符串
    private String getArrayString(float[] values) {
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<values.length;i++){
            sb.append(values[i]+"  ");
        }
        return sb.toString();
    }

}