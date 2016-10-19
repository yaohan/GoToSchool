package com.example.bluetoothscan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 传感器页面
 */
public class SensorActivity extends Activity {
    //传感器类型
    private int[] types ={Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_MAGNETIC_FIELD, Sensor.TYPE_ORIENTATION, Sensor.TYPE_GYROSCOPE, Sensor.TYPE_LIGHT,
            Sensor.TYPE_PRESSURE, Sensor.TYPE_TEMPERATURE, Sensor.TYPE_PROXIMITY, Sensor.TYPE_GRAVITY, Sensor.TYPE_LINEAR_ACCELERATION,
            Sensor.TYPE_ROTATION_VECTOR, Sensor.TYPE_RELATIVE_HUMIDITY, Sensor.TYPE_AMBIENT_TEMPERATURE, Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED, Sensor.TYPE_GAME_ROTATION_VECTOR,
            Sensor.TYPE_GYROSCOPE_UNCALIBRATED, Sensor.TYPE_SIGNIFICANT_MOTION, Sensor.TYPE_STEP_DETECTOR, Sensor.TYPE_STEP_COUNTER, Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR};
    //对应的中文名
    private String[] sensors = {"加速度传感器","磁场传感器","方向传感器","陀螺仪传感器","亮度传感器",
            "压力传感器", "温度传感器", "距离传感器", "重力传感器", "线性加速度传感器",
            "旋转矢量传感器", "湿度传感器", "温度传感器", "无标定磁场传感器", "无标定旋转矢量传感器",
            "未校准陀螺仪传感器", "特殊动作触发传感器", "步行检测传感器", "计步器传感器", "地磁旋转矢量传感器"};
    //传感器列表
    private List<Sensor> list;

    private ListView listView;
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
        setContentView(R.layout.activity_sensor);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("传感器");
        //获取传感器manager
        SensorManager sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        //获取所有传感器列表
        list = sensorManager.getSensorList(Sensor.TYPE_ALL);
        if(list.size() == 0){
            ((TextView)findViewById(R.id.text)).setText("没有数据");
        }
        list = reGet(list);
        //根据type大小进行排序
        Collections.sort(list, new Comparator<Sensor>() {
            @Override
            public int compare(Sensor sensor, Sensor t1) {
                return sensor.getType() - t1.getType();
            }
        });
        //绑定控件
        listView = (ListView)findViewById(R.id.lv_list);
        //绑定listView数据源
        final MyAdapter adapter = new MyAdapter(this);
        listView.setAdapter(adapter);
        //listView点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //跳转到对应传感器的详细信息页
                Intent intent = new Intent(SensorActivity.this, SensorDetailActivity.class);
                intent.putExtra("type",list.get(i).getType());
                intent.putExtra("string",getInfo(list.get(i),((TextView) view.findViewById(R.id.detail)).getText().toString()));
                startActivity(intent);
            }
        });
    }

    //获取传感器信息
    private String getInfo(Sensor sensor, String name){
        StringBuffer sb = new StringBuffer();
        sb.append("名称：" + name + "\n");
        sb.append("name："+sensor.getName()+"\n");
        sb.append("供应商：" + sensor.getVendor() + "\n");
        return sb.toString();
    }

    //getSensorList获取到的AbstractList不能进行排序操作，将其拷贝到ArrayList中
    private List<Sensor> reGet(List<Sensor> list) {
        List<Sensor> newList = new ArrayList<>();
        for(Sensor sensor:list){
            newList.add(sensor);
        }
        return newList;
    }

    //绑定ListView数据源的Adapter
    class MyAdapter extends BaseAdapter{
        private Context context;
        public MyAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null){
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                view = layoutInflater.inflate(R.layout.item_detail,null);
            }
            TextView textView = (TextView)view.findViewById(R.id.detail);
            textView.setText(getTypeName(list.get(i)));
            return view;
        }

        private String getTypeName(Sensor sensor) {
            for(int i=0;i<types.length;i++){
                if(sensor.getType() == types[i]){
                    return sensors[i];
                }
            }
            return sensor.getType()+"";
        }

    }
}
