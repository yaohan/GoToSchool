/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ssdut411.app.bookbar.zxing.decoding;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.ssdut411.app.bookbar.R;
import com.ssdut411.app.bookbar.activity.mainPage.BookDetailActivity;
import com.ssdut411.app.bookbar.activity.mainPage.CaptureActivity;
import com.ssdut411.app.bookbar.activity.show.FindBookActivity;
import com.ssdut411.app.bookbar.activity.system.MainApplication;
import com.ssdut411.app.bookbar.utils.ActivityStackUtils;
import com.ssdut411.app.bookbar.utils.L;
import com.ssdut411.app.bookbar.utils.T;
import com.ssdut411.app.bookbar.zxing.camera.CameraManager;
import com.ssdut411.app.bookbar.zxing.view.ViewfinderResultPointCallback;

import java.util.Vector;

/**
 * This class handles all the messaging which comprises the state machine for capture.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class CaptureActivityHandler extends Handler {

  @SuppressWarnings("unused")
private static final String TAG = CaptureActivityHandler.class.getSimpleName();

  private final CaptureActivity activity;
  private final DecodeThread decodeThread;
  private State state;


  private enum State {
    PREVIEW,
    SUCCESS,
    DONE
  }

  public CaptureActivityHandler(CaptureActivity activity, Vector<BarcodeFormat> decodeFormats,
      String characterSet) {
    this.activity = activity;
    decodeThread = new DecodeThread(activity, decodeFormats, characterSet,
        new ViewfinderResultPointCallback(activity.getViewfinderView()));
    decodeThread.start();
    state = State.SUCCESS;

    // Start ourselves capturing previews and decoding.
    CameraManager.get().startPreview();
    restartPreviewAndDecode();
  }

  @Override
  
  /*
   *	鎵弿缁撴灉鐨勬秷鎭鐞� 
   */
  public void handleMessage(Message message) {
    switch (message.what) {
      case R.id.auto_focus:
        if (state == State.PREVIEW) {
          CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
        }
        break;
      case R.id.restart_preview:
        restartPreviewAndDecode();
        break;
      //鎵弿鎴愬姛鐨勬秷鎭�
      case R.id.decode_succeeded:
        state = State.SUCCESS;
        
        Bundle bundle = message.getData();
        Bitmap barcode = bundle == null ? null :(Bitmap) bundle.getParcelable(DecodeThread.BARCODE_BITMAP);
        
        String str_result=((Result) message.obj).getText();
        activity.handleDecode((Result) message.obj, barcode);

        ActivityStackUtils.getInstance().finishActivity(FindBookActivity.class);
        //鑾峰彇鍒癐SBN鐮佸悗杩斿洖鍒颁富Activity
    	final Intent intent=new Intent(activity,BookDetailActivity.class);
//        L.i("result："+str_result);
        intent.putExtra("result", str_result);
        MainApplication.getInstance().setHasScan(true);

        if(MainApplication.getInstance().isDirectBorrow()){
          ActivityStackUtils.getInstance().finishActivity(BookDetailActivity.class);
          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              MainApplication.getInstance().setDirectBorrow(true);
              activity.startActivity(intent);
              activity.finish();
            }
          },1000);
        }else{
          ActivityStackUtils.getInstance().finishActivity(BookDetailActivity.class);
          activity.startActivity(intent);
          activity.finish();
        }
        break;
        
      case R.id.decode_failed:
//        Log.i("OUTPUT", "Got return scan result message failed");
        state = State.PREVIEW;
        CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
        break;
      case R.id.return_scan_result:
//        Log.i("OUTPUT", "Got return scan result message scan result");
//    	Intent intent2=new Intent(activity,BookDetailActivity.class);
//		activity.startActivity(intent2);
        break;
    }
  }

  public void quitSynchronously() {
    state = State.DONE;
    CameraManager.get().stopPreview();
    Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
    quit.sendToTarget();
    try {
      decodeThread.join();
    } catch (InterruptedException e) {
      // continue
    }

    // Be absolutely sure we don't send any queued up messages
    removeMessages(R.id.decode_succeeded);
    //removeMessages(R.id.return_scan_result);
    removeMessages(R.id.decode_failed);
  }

  private void restartPreviewAndDecode() {
    if (state == State.SUCCESS) {
      state = State.PREVIEW;
      CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
      CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
      activity.drawViewfinder();
    }
  }

}
