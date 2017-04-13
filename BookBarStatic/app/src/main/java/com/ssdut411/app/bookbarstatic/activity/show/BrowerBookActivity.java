package com.ssdut411.app.bookbarstatic.activity.show;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.widget.RelativeLayout;


import com.ssdut411.app.bookbarstatic.R;
import com.ssdut411.app.bookbarstatic.activity.system.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by LENOVO on 2016/10/23.
 */
public class BrowerBookActivity extends BaseActivity {
    private List<WifiInfo> wifiInfoList;
    private Timer timer = new Timer();;
    private BrowerBookView browerBookView;
    private Location nowLocation;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (nowLocation != null) {
                browerBookView.drawCircle(nowLocation.getX(), nowLocation.getY());
            }
        }
    };

    @Override
    protected String initTitle() {
        return "定位图书";
    }

    @Override
    protected int initMenu() {
        return 0;
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_browerbook;
    }

    @Override
    protected void initViews() {
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.layout);
        browerBookView = new BrowerBookView(BrowerBookActivity.this);
        layout.addView(browerBookView);
        startScanWIFI();
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void showView() {

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

            }
        }, 1000, 1000);
    }
    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }
}
