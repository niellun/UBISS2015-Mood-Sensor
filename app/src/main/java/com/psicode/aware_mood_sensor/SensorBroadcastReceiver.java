package com.psicode.aware_mood_sensor;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;

import com.aware.Accelerometer;
import com.aware.Gravity;
import com.aware.Gyroscope;
import com.aware.LinearAccelerometer;
import com.aware.Rotation;
import com.aware.providers.Accelerometer_Provider;
import com.aware.providers.Gravity_Provider;
import com.aware.providers.Linear_Accelerometer_Provider;
import com.aware.providers.Rotation_Provider;

import java.util.Calendar;

/**
 * Created by niellune on 09.06.15.
 */
public class SensorBroadcastReceiver extends BroadcastReceiver {

    private final Context _context;
    private String _label;
    private volatile boolean _needSet;
    private volatile boolean _needReset;

    private double[] mGravity = {0, 0, 0};
    private float[] mGlobal = {0, 0, 0};
    private double[] mAccel = {0, 0, 0};
    private double[] mRot = {0, 0, 0};
    private double accel = 0;
    private double sec = 0;
    private int count = 0;
    private double last = 0;


    public SensorBroadcastReceiver(Context context) {
        _context = context;
        _label = "";
        _needReset = false;
    }

    public void SetSensorLabels(String label) {
        _context.sendBroadcast(new Intent(LinearAccelerometer.ACTION_AWARE_LINEAR_LABEL)
                .putExtra(LinearAccelerometer.EXTRA_LABEL, label));

        _context.sendBroadcast(new Intent(Gyroscope.ACTION_AWARE_GYROSCOPE_LABEL)
                .putExtra(Gyroscope.EXTRA_LABEL, label));

        _context.sendBroadcast(new Intent(Rotation.ACTION_AWARE_ROTATION_LABEL)
                .putExtra(Rotation.EXTRA_LABEL, label));

        _context.sendBroadcast(new Intent(Gravity.ACTION_AWARE_GRAVITY_LABEL)
                .putExtra(Gravity.EXTRA_LABEL, label));
    }

    public void Start(String label) {
        Log.d("BR", "Start");

        _label = label;
        _needSet = true;
        _needReset = false;
    }

    public void Stop() {
        Log.d("BR", "Stop");

        _needSet = false;
        _needReset = false;
        SetSensorLabels("");
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() == LinearAccelerometer.ACTION_AWARE_LINEAR_ACCELEROMETER) {

            ContentValues cv = intent.getParcelableExtra(LinearAccelerometer.EXTRA_DATA);
            double x = cv.getAsDouble(Linear_Accelerometer_Provider.Linear_Accelerometer_Data.VALUES_0);
            double y = cv.getAsDouble(Linear_Accelerometer_Provider.Linear_Accelerometer_Data.VALUES_1);
            double z = cv.getAsDouble(Linear_Accelerometer_Provider.Linear_Accelerometer_Data.VALUES_2);
            accel = x * x + y * y + z * z;
            mAccel[0] = x;
            mAccel[1] = y;
            mAccel[2] = z;
        }

        if (intent.getAction() == GlobalAccel.ACTION_NEW_DATA) {
            ContentValues cv = intent.getParcelableExtra(LinearAccelerometer.EXTRA_DATA);
            mGlobal[0] = intent.getFloatExtra(GlobalAccel.X, 0);
            mGlobal[1] = intent.getFloatExtra(GlobalAccel.Y, 0);
            mGlobal[2] = intent.getFloatExtra(GlobalAccel.Z, 0);
        }

        if (intent.getAction() == Rotation.ACTION_AWARE_ROTATION) {
            ContentValues cv = intent.getParcelableExtra(Rotation.EXTRA_DATA);
            mRot[0] = cv.getAsDouble(Rotation_Provider.Rotation_Data.VALUES_0);
            mRot[1] = cv.getAsDouble(Rotation_Provider.Rotation_Data.VALUES_1);
            mRot[2] = cv.getAsDouble(Rotation_Provider.Rotation_Data.VALUES_2);
        }

        if (intent.getAction() == Gravity.ACTION_AWARE_GRAVITY) {
            ContentValues cv = intent.getParcelableExtra(Gravity.EXTRA_DATA);
            mGravity[0] = cv.getAsDouble(Gravity_Provider.Gravity_Data.VALUES_0);
            mGravity[1] = cv.getAsDouble(Gravity_Provider.Gravity_Data.VALUES_1);
            mGravity[2] = cv.getAsDouble(Gravity_Provider.Gravity_Data.VALUES_2);
        }

        double seconds = System.currentTimeMillis()/1000;

        if(accel<50 || accel>200)
            return;

        if (Model()) {
            Log.d("MODEL", "cnt "+count+" last "+(seconds-last)+" sound "+(seconds-sec));

            if(seconds-last<2)
            {
                count++;
            }
            else
            {
                count = 1;
            }

            synchronized (mAccel) {
                if (seconds - sec > 3 && count > 10) {
                    sec = seconds;
                    Log.d("MODEL", "sound");
                    MediaPlayer.create(_context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)).start();
                }
            }
            last = seconds;
        }
    }


    public boolean Model() {

        if (mGravity[0] < 0.49) {
            if (mGravity[2] < -4.43) {
                if (mRot[0] < 0.78) {
                    if (mGravity[1] < 8.63) {
                        return true;
                    }
                    if (mGravity[1] >= 8.63) {
                        return false;
                    }
                }
                if (mRot[0] >= 0.78) {
                    if (mRot[1] < -0.16) {
                        return false;
                    }
                    if (mRot[1] >= -0.16) {
                        return true;
                    }
                }
            }
            if (mGravity[2] >= -4.43) {
                if (accel < 1.5) {
                    if (mAccel[1] < -0.22) {
                        return false;
                    }
                    if (mAccel[1] >= -0.22) {
                        return false;
                    }
                }
                if (accel >= 1.5) {
                    if (mGlobal[2] < -5.55) {
                        return true;
                    }
                    if (mGlobal[2] >= -5.55) {
                        return false;
                    }
                }
            }
        }
        if (mGravity[0] >= 0.49) {
            if (mGravity[2] < -4.44) {
                if (mAccel[0] < 5.19) {
                    if (mGlobal[1] < 2.81) {
                        return true;
                    }
                    if (mGlobal[1] >= 2.81) {
                        return true;
                    }
                }
                if (mAccel[0] >= 5.19) {
                    if (mAccel[1] < -4.28) {
                        return true;
                    }
                    if (mAccel[1] >= -4.28) {
                        return false;
                    }
                }
            }
            if (mGravity[2] >= -4.44) {
                if (mAccel[0] < -1.22) {
                    if (mGravity[2] < 8.69) {
                        return true;
                    }
                    if (mGravity[2] >= 8.69) {
                        return true;
                    }
                }
                if (mAccel[0] >= -1.22) {
                    if (mAccel[2] < 1.26) {
                        return false;
                    }
                    if (mAccel[2] >= 1.26) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
