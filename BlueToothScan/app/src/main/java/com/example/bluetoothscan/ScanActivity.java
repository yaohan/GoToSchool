package com.example.bluetoothscan;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by yao_han on 2016/10/8.
 */
public class ScanActivity extends Activity {
    private Context mContext = this;
    private String TAG = "yao";
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;
    private Boolean mScanning;
    private int SCAN_PERIOD =5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

        TextView textView = (TextView)findViewById(R.id.tv);
        Button bt = (Button)findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanLeDevice(true);
            }
        });
    }

    public boolean initialize() {
        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Log.e(TAG, "Unable to initialize Bluetooth.");
            return false;
        }
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    public boolean OpenBlue() {
        //开启蓝牙
        if (!mBluetoothAdapter.isEnabled())
            return mBluetoothAdapter.enable();
        else
            return true;
    }

    private void scanLeDevice(final boolean enable) {
        if (enable)//enable =true就是说要开始扫描
        {
            // Stops scanning after a pre-defined scan period.
            // 下边的代码是为了在 SCAN_PERIOD 后，停止扫描的
            // 如果需要不停的扫描，可以注释掉
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mScanning = false;
//                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                    // 这个是重置menu，将 menu中的停止按钮->扫描按钮
//                    invalidateOptionsMenu();
//                }
//            }, SCAN_PERIOD);

            mScanning = true;//此变量指示扫描是否进行
            mBluetoothAdapter.startLeScan(mLeScanCallback);//这句就是开始扫描了
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);//这句就是停止扫描
        }
        // 这个是重置menu，将 menu中的扫描按钮->停止按钮
        invalidateOptionsMenu();
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.i(TAG,"device:"+device.getName()+"   rssi:"+rssi+"scanRecord:"+scanRecord.toString());
        }

    };
}
