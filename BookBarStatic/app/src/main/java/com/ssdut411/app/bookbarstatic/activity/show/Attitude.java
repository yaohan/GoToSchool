package com.ssdut411.app.bookbarstatic.activity.show;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;

public class Attitude implements SensorEventListener {
    SensorManager sensorManager = null;
    float[] values;

    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private float[] mAccelerometer = new float[3];
    private float[] mMatrix = new float[9];
    private float[] mGyroscope = new float[3];
    private float[] attitude = new float[4];
    private float[] attitideFromAndroidAPI = new float[4];

    float Fw = 0f;// 角速度造成的误差，单位弧度
    float Fa = 0f;// 加速度造成的误差，单位弧度

    boolean hasEnoughError = true;//当误差>5°时，认为误差够大可以校准
    boolean gyroscopeInSafeRange = false;//角速度是否在安全范围内
    boolean accelerometerInSafeRange = false;//加速度是否在安全范围内

    int maxWindowSize = 50; // 探测窗口大小,每秒50个
    int currentWindowIndexOfCompass = 0; // 指南针当前下标
    int currentWindowIndexOfGyroscope = 0; // 陀螺仪当前下标
    float[][] orientationsFromCompass = new float[maxWindowSize][3];
    float[][] orientationsFromGyroscope = new float[maxWindowSize][3];

    //静止时陀螺仪读数不为0，在window范围内判断是否静止
    //数值小于0.1，窗口内方差小，则静止
    float[] gyroscopeToZero = new float[3];
    int maxGyroWindowSize = 100;
    int currentGyroscope = 0;
    float[][] gyroscopeValues = new float[maxGyroWindowSize][3];

    int calibrationOpportunities = 0;
    float minP = 0.2f;//p大于多少时认为相似度高

    static String TYPE_COMPASS = "compass";
    static String TYPE_GYROSCOPE = "gyroscope";

    public BrowerBookView mv;
    public TextView txt1;
    public TextView txt2;
    int a,b,c,d;

    public Attitude(Context context){
        sensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
    }

    public void start(){
        rewrite("attitudes.txt");
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_GAME);
    }
    public void stop() {
        sensorManager.unregisterListener(this);
    }

    private void setHeading(float ori){
        mv.setHeading(ori);
    }

    float timestamp;
    @SuppressLint("NewApi") @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.97f;

        synchronized (this) {
            values = event.values;
            long time = event.timestamp;
            //磁场传感器
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                mGeomagnetic = event.values;
                float RR[] = new float[9];
                float I[] = new float[9];
                boolean success = SensorManager.getRotationMatrix(RR, I,
                        mGravity, mGeomagnetic);
                //根据指南针成功获取到了旋转矩阵
                if (success) {
                    float orientation[] = new float[3]; // orientation[0]:来自指南针和重力的方向
                    SensorManager.getOrientation(RR, orientation);
                    mMatrix = RR;
                    //初始时
                    if ((attitude[0] == attitude[1]
                            && attitude[0] == attitude[2] && attitude[0] == 0)
                            && (mMatrix[0] != 0 && mMatrix[1] != 0)) {
                        // 用指南针+重力导出的姿态校准姿态
                        attitude = getQuaternionFromMatrix(mMatrix);
                        setHeading(orientation[0]);
//						txt1.setText("orientation:" + Math.toDegrees(orientation[0]));
                        attitideFromAndroidAPI=getQuaternionFromMatrix(mMatrix);
                        return;
                    }

                    //弧度->角度
                    //(-180,180)->(0,360)
                    orientation[0] = (float) Math.toDegrees(orientation[0]);
                    if(orientation[0]<0){
                        orientation[0]=360f+orientation[0];
                    }
                    //校准窗口
                    orientationsFromCompass[currentWindowIndexOfCompass++] = orientation;
                    if (currentWindowIndexOfCompass == maxWindowSize) {
                        currentWindowIndexOfGyroscope = 0;
                        currentWindowIndexOfCompass = 0;
                        float[] differences = new float[maxWindowSize];// 两个方向的差
                        for (int i = 0; i < maxWindowSize; i++) {
                            differences[i] = orientationsFromCompass[i][0]
                                    - orientationsFromGyroscope[i][0];
                        }
                        //校准机会
                        if((1/Math.pow(2, getVariance(differences,maxWindowSize)))>minP&&hasEnoughError&&accelerometerInSafeRange&&gyroscopeInSafeRange){
                            hasEnoughError=false;
                            Fa=0;
                            Fw=0;
                            attitude = getQuaternionFromMatrix(mMatrix);// 用指南针+重力导出的姿态校准姿态
                            calibrationOpportunities++;
                        }
                    }
                }
            }

            if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                float[] oldValue =new float[3];
                oldValue[0]=values[0];
                oldValue[1]=values[1];
                oldValue[2]=values[2];
                gyroscopeValues[currentGyroscope++] = oldValue;
//				//使静止时读数为0
                values[0]+=gyroscopeToZero[0];
                values[1]+=gyroscopeToZero[1];
                values[2]+=gyroscopeToZero[2];
                if(currentGyroscope==100){
                    currentGyroscope = 0 ;
                    float[]gyroX = new float[maxGyroWindowSize];
                    float[]gyroY = new float[maxGyroWindowSize];
                    float[]gyroZ = new float[maxGyroWindowSize];
                    for(int i=0;i<maxGyroWindowSize;i++){
                        gyroX[i]=gyroscopeValues[i][0];
                        gyroY[i]=gyroscopeValues[i][1];
                        gyroZ[i]=gyroscopeValues[i][2];
                    }
                    float varianceX=getVariance(gyroX, maxGyroWindowSize)*100000f;
                    float varianceY=getVariance(gyroY, maxGyroWindowSize)*100000f;
                    float varianceZ=getVariance(gyroZ, maxGyroWindowSize)*100000f;
                    float maxX=getMax(gyroX,maxGyroWindowSize);
                    float maxY=getMax(gyroY,maxGyroWindowSize);
                    float maxZ=getMax(gyroZ,maxGyroWindowSize);
                    if(varianceZ<1&&varianceX<1&&varianceY<1&&maxX<0.1&&maxY<0.1&&maxZ<0.1){
                        gyroscopeToZero[0]=-getAverage(gyroX,maxGyroWindowSize);
                        gyroscopeToZero[1]=-getAverage(gyroY,maxGyroWindowSize);
                        gyroscopeToZero[2]=-getAverage(gyroZ,maxGyroWindowSize);
                    }
                }
                gyroscopeInSafeRange = getMax(values)*180/Math.PI<240;
                mGyroscope[0] = -values[0] * 0.02f;
                mGyroscope[1] = -values[1] * 0.02f;
                mGyroscope[2] = -values[2] * 0.02f;
                if (attitude[0] != 0 && attitude[1] != 0) {
                    attitude = multiplyQuaternions(attitude,
                            getQuaternionFromEuler(mGyroscope));
                    float orientation[] = new float[3]; // orientation[0]:来自陀螺仪的方向
                    SensorManager.getOrientation(
                            getRotationMatrixFromQuaternion(attitude),
                            orientation);
                    setHeading(orientation[0]);
//					txt1.setText("orientation:"+Math.toDegrees(orientation[0]));
                    //弧度->角度
                    //(-180,180)->(0,360)
                    orientation[0] = (float) Math.toDegrees(orientation[0]);
                    if(orientation[0]<0){
                        orientation[0]=360f+orientation[0];
                    }

                    //校准窗口
                    orientationsFromGyroscope[currentWindowIndexOfGyroscope++] = orientation;
                    if (currentWindowIndexOfGyroscope == maxWindowSize) {
                        currentWindowIndexOfGyroscope = 0;
                        currentWindowIndexOfCompass = 0;
                        float[] differences = new float[maxWindowSize];// 两个方向的差
                        for (int i = 0; i < maxWindowSize; i++) {
                            differences[i] = orientationsFromCompass[i][0]
                                    - orientationsFromGyroscope[i][0];
                        }
                        if((1/Math.pow(2, getVariance(differences,maxWindowSize)))>minP&&hasEnoughError&&accelerometerInSafeRange&&gyroscopeInSafeRange){
                            hasEnoughError=false;
                            Fa=0;
                            Fw=0;
                            attitude = getQuaternionFromMatrix(mMatrix);// 用指南针+重力导出的姿态校准姿态
                            calibrationOpportunities++;
                        }
                    }
                }
                Fw = (float) (Fw + (getMax(values) * 180 * 0.02 * 0.0003)
                        / Math.PI);
                if (Fw + Fa > 5) {
                    hasEnoughError = true;
                }
            }

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                accelerometerInSafeRange = getMax(values)<19.6f;
            }
            if(event.sensor.getType()== Sensor.TYPE_GRAVITY){
                mGravity[0] = event.values[0];
                mGravity[1] = event.values[1];
                mGravity[2] = event.values[2];
                Fa = (float) (Fa + (getMax(mGravity) * 0.02f * 0.001f * 180)
                        / Math.PI);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    float getAverage(float[] arr,int size){
        float sum =0;
        for (int i = 0; i < size; i++) {
            sum += arr[i];
        }
        return sum/size;
    }

    float getVariance(float[] arr,int size) {
        float sum = 0;
        float average = 0;
        for (int i = 0; i < size; i++) {
            sum += arr[i];
        }
        average = sum / size;
        sum = 0;
        for (int i = 0; i < size; i++) {
            sum = sum + ((arr[i] - average) * (arr[i] - average));
        }
        return sum / size;
    }

    // 0123对应xyzw
    float[] multiplyQuaternions(float[] a, float[] b) {
        float object[] = new float[4];
        float qax = a[0], qay = a[1], qaz = a[2], qaw = a[3];
        float qbx = b[0], qby = b[1], qbz = b[2], qbw = b[3];

        object[0] = qax * qbw + qaw * qbx + qay * qbz - qaz * qby;
        object[1] = qay * qbw + qaw * qby + qaz * qbx - qax * qbz;
        object[2] = qaz * qbw + qaw * qbz + qax * qby - qay * qbx;
        object[3] = qaw * qbw - qax * qbx - qay * qby - qaz * qbz;
        return object;
    }

    float[] getRotationMatrixFromQuaternion(float[] quaternion) {
        float qx = quaternion[0], qy = quaternion[1], qz = quaternion[2], qw = quaternion[3];
        float object[] = new float[9];
        object[0] = 1 - 2 * qy * qy - 2 * qz * qz;
        object[1] = 2 * qx * qy + 2 * qz * qw;
        object[2] = 2 * qx * qz - 2 * qy * qw;
        object[3] = 2 * qx * qy - 2 * qz * qw;
        object[4] = 1 - 2 * qx * qx - 2 * qz * qz;
        object[5] = 2 * qy * qz + 2 * qx * qw;
        object[6] = 2 * qx * qz + 2 * qy * qw;
        object[7] = 2 * qy * qz - 2 * qx * qw;
        object[8] = 1 - 2 * qx * qx - 2 * qy * qy;

        return object;
    }

    float[] getQuaternionFromEuler(float[] euler) {
        float object[] = new float[4];
        float c1 = (float) Math.cos(euler[0] / 2);
        float c2 = (float) Math.cos(euler[1] / 2);
        float c3 = (float) Math.cos(euler[2] / 2);
        float s1 = (float) Math.sin(euler[0] / 2);
        float s2 = (float) Math.sin(euler[1] / 2);
        float s3 = (float) Math.sin(euler[2] / 2);
        object[0] = s1 * c2 * c3 + c1 * s2 * s3;
        object[1] = c1 * s2 * c3 - s1 * c2 * s3;
        object[2] = c1 * c2 * s3 + s1 * s2 * c3;
        object[3] = c1 * c2 * c3 - s1 * s2 * s3;
        return object;
    }

    float[] getQuaternionFromMatrix(float[] m) {// 正确
        float object[] = new float[9];

        float m11 = m[0], m12 = m[3], m13 = m[6];
        float m21 = m[1], m22 = m[4], m23 = m[7];
        float m31 = m[2], m32 = m[5], m33 = m[8];
        float trace = m11 + m22 + m33;
        float s;

        if (trace > 0) {
            s = (float) (0.5 / Math.sqrt(trace + 1.0));

            object[3] = 0.25f / s;
            object[0] = (m32 - m23) * s;
            object[1] = (m13 - m31) * s;
            object[2] = (m21 - m12) * s;
        } else if (m11 > m22 && m11 > m33) {

            s = (float) (2.0 * Math.sqrt(1.0 + m11 - m22 - m33));

            object[3] = (m32 - m23) / s;
            object[0] = 0.25f * s;
            object[1] = (m12 + m21) / s;
            object[2] = (m13 + m31) / s;

        } else if (m22 > m33) {

            s = (float) (2.0 * Math.sqrt(1.0 + m22 - m11 - m33));

            object[3] = (m13 - m31) / s;
            object[0] = (m12 + m21) / s;
            object[1] = 0.25f * s;
            object[2] = (m23 + m32) / s;

        } else {

            s = (float) (2.0 * Math.sqrt(1.0 + m33 - m11 - m22));

            object[3] = (m21 - m12) / s;
            object[0] = (m13 + m31) / s;
            object[1] = (m23 + m32) / s;
            object[2] = 0.25f * s;

        }

        return object;
    }

    float getMax(float aa[]) {
        float[]a=new float[3];
        a[0] = aa[0] > 0 ? aa[0] : -aa[0];
        a[1] = aa[1] > 0 ? aa[1] : -aa[1];
        a[2] = aa[2] > 0 ? aa[2] : -aa[2];
        if (a[0] > a[1]) {
            if (a[0] > a[2]) {
                return a[0];
            }
            return a[2];
        }
        if (a[1] > a[2]) {
            return a[1];
        }
        return a[2];
    }
    float getMax(float aa[],int size){
        float max = -10000000;
        for(int i=0;i<size;i++){
            float abs = aa[i]>0?aa[i]:-aa[i];
            if(abs>max){
                max=abs;
            }
        }
        return max;
    }
    private void write(String name, String content) {
        try {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED))
                ;
            {
                File sdcardDir = new File(Environment
                        .getExternalStorageDirectory().toString() + "/毕业设计/");
                if (!sdcardDir.exists()) {
                    sdcardDir.mkdir();
                }
                File targetfFile = new File(sdcardDir, name);
                RandomAccessFile raf = new RandomAccessFile(targetfFile, "rw");
                raf.seek(targetfFile.length());
                raf.write(content.getBytes());
                raf.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rewrite(String name) {
        try {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED))
                ;
            {
                File sdcardDir = new File(Environment
                        .getExternalStorageDirectory().toString() + "/毕业设计/");
                if (!sdcardDir.exists()) {
                    sdcardDir.mkdir();
                }
                FileOutputStream fos = new FileOutputStream(
                        sdcardDir.getCanonicalPath() + "/" + name);
                PrintStream ps = new PrintStream(fos);
                ps.println("");
                ps.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
