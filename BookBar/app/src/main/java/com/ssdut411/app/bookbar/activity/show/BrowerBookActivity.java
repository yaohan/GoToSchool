package com.ssdut411.app.bookbar.activity.show;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.utils.T;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by LENOVO on 2016/10/23.
 */
public class BrowerBookActivity extends Activity {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browerbook);
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.layout);
        browerBookView = new BrowerBookView(BrowerBookActivity.this);
        layout.addView(browerBookView);
        startScanWIFI();
        final EditText bookName = (EditText)findViewById(R.id.et_book);
        Button btSubmit = (Button)findViewById(R.id.bt_submit);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nowLocation == null){
                    T.showShort(BrowerBookActivity.this,"定位失败");
                }else{
                    String book = bookName.getText().toString();
                    if(book.length() == 0){
                        T.showShort(BrowerBookActivity.this, "书名不能为空");
                    }else{
                        DBFile.addBook(book,nowLocation);
                    }
                }
            }
        });
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
                Location location = DBFile.searchLocation(wifiInfoList);
                nowLocation = location;
                Message msg = new Message();
                handler.sendMessage(msg);

            }
        }, 1000, 1000);
    } @Override
      protected void onPause() {
        super.onPause();
        timer.cancel();
    }
}
