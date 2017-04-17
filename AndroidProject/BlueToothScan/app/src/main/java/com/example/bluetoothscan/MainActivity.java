package com.example.bluetoothscan;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    public int REQUEST_ENABLE_BT = 1;
    public TextView textView;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @TargetApi(Build.VERSION_CODES.ECLAIR)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
//            Log.i("yao","onReceive action:"+action);

            //当设备开始扫描时。
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                Log.i("yao","action:"+action);
                //从Intent得到blueDevice对象
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.i("yao","address:"+device.getAddress()+"  rssi:"+intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI));
                textView.setText("address:"+device.getAddress()+"  rssi:"+intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI));
//                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
//                    String name = intent.getExtras().getString(BluetoothDevice.EXTRA_NAME);
//                    String uuid = intent.getExtras().getString(BluetoothDevice.EXTRA_UUID);
//                    short rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
//                    textView.setText(textView.getText() + "\n" + " name:" + name + " uuid:" + uuid + " rssi:" + rssi);
//
//                }

            }
        }
    };

    private IntentFilter filter,foundFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.tv);
        Button bt = (Button)findViewById(R.id.bt);
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!bluetoothAdapter.isEnabled()){
            Log.i("yao","请求开启蓝牙");
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent,REQUEST_ENABLE_BT);
        }else{
            Log.i("yao","蓝牙已经开启");
        }

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothAdapter.startDiscovery();
            }
        });

//        if(bluetoothAdapter == null){
//            Log.i("yao","不支持蓝牙");
//        }else{
//            Log.i("yao","支持蓝牙");
//        }


        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
//        foundFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//        registerReceiver(mReceiver, foundFilter);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("yao","onActivityResult"+"requestCode=="+requestCode);
        if(requestCode == REQUEST_ENABLE_BT){
            if(resultCode == RESULT_OK){
                Log.i("yao","蓝牙已经开启");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
