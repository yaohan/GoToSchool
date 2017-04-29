package com.ssdut411.androidsoundrecord;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends Activity {

    private int frequency = 44100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏状态栏
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        // 把Activity的标题去掉
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//        // 设置布局
//        setContentView(new MySurfaceView(this));
        setContentView(R.layout.activity_main);
        TextView a = new TextView(this);
//        try {
//            int minBufferSize = AudioRecord.getMinBufferSize(frequency, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
//            AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize);
//            audioRecord.startRecording();
//            short[] buffer = new short[minBufferSize];
//            int bufferRaadResult = audioRecord.read(buffer, 0, minBufferSize);
//            StringBuffer stringBuffer = new StringBuffer();
//            for (short s : buffer) {
//                stringBuffer.append(s + " ");
//            }
//            Log.i("buffer",stringBuffer.toString());
//        }catch(Exception e){
//            e.printStackTrace();
//        }
    }
}
