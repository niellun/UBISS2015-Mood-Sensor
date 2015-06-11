package com.psicode.aware_mood_sensor;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.Matrix;
import android.util.Log;

import com.aware.Accelerometer;
import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.LinearAccelerometer;

/**
 * Created by niellune on 11.06.15.
 */
public class GlobalAccel implements SensorEventListener {

    private float[] mGravity;
    private float[] mMagnetic;
    private Context mContext;
    private SensorManager mSensorManager;
    private Sensor mLinearAccelSensor;
    private Sensor mGravitySensor;
    private Sensor mMagneticSensor;
    public static String Label = "";

    public static final String ACTION_NEW_DATA = "com.aware.sensor.global_accelerometer.broadcast";
    public static final String X = "X_VALUE";
    public static final String Y = "Y_VALUE";
    public static final String Z = "Z_VALUE";

    public static class Labeler extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            Label = intent.getStringExtra(LinearAccelerometer.EXTRA_LABEL);
        }
    }

    public GlobalAccel(Context context) {
        mContext = context;

        mGravity = new float[4];
        mMagnetic = new float[4];

        for (int i = 0; i < 4; i++) {
            mGravity[i] = 0;
            mMagnetic[i] = 0;
        }

        mSensorManager = (SensorManager) mContext.getSystemService(mContext.SENSOR_SERVICE);
        mLinearAccelSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mGravitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mMagneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(this, mLinearAccelSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void Destroy()
    {
        mSensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] trueAcceleration = new float[4];

        synchronized (mGravity) {

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                mMagnetic[0] = event.values[0];
                mMagnetic[1] = event.values[1];
                mMagnetic[2] = event.values[2];
            }

            if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
                float[] R = new float[16];
                float[] RINV = new float[16];
                float[] acc = new float[]{ event.values[0], event.values[1], event.values[2], 0};

                SensorManager.getRotationMatrix(R, null, mGravity, mMagnetic);
                Matrix.invertM(RINV, 0, R, 0);
                Matrix.multiplyMV(trueAcceleration, 0, RINV, 0, acc, 0);
            }

            if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
                mGravity[0] = event.values[0];
                mGravity[1] = event.values[1];
                mGravity[2] = event.values[2];
            }
        }

        if(event.sensor.getType()==Sensor.TYPE_LINEAR_ACCELERATION)
        {
            Log.d("GlobalAccel", "X="+trueAcceleration[0]+" Y="+trueAcceleration[1]+" Z="+trueAcceleration[2]);

            ContentValues data = new ContentValues();
            data.put(GlobalAccelProvider.GlobalAccel_Data.TIMESTAMP, System.currentTimeMillis());
            data.put(GlobalAccelProvider.GlobalAccel_Data.DEVICE_ID, Aware.getSetting(mContext, Aware_Preferences.DEVICE_ID));
            data.put(GlobalAccelProvider.GlobalAccel_Data.VALUE_X, trueAcceleration[0]);
            data.put(GlobalAccelProvider.GlobalAccel_Data.VALUE_Y, trueAcceleration[1]);
            data.put(GlobalAccelProvider.GlobalAccel_Data.VALUE_Z, trueAcceleration[2]);
            mContext.getContentResolver().insert(GlobalAccelProvider.GlobalAccel_Data.CONTENT_URI, data);

            //TODO Broadcast
            Intent intent = new Intent();
            intent.setAction(ACTION_NEW_DATA);
            intent.putExtra(X, trueAcceleration[0]);
            intent.putExtra(Y, trueAcceleration[1]);
            intent.putExtra(Z, trueAcceleration[2]);
            mContext.sendBroadcast(intent);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
