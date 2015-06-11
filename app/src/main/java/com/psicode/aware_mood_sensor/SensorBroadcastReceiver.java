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
import com.aware.providers.Linear_Accelerometer_Provider;

/**
 * Created by niellune on 09.06.15.
 */
public class SensorBroadcastReceiver extends BroadcastReceiver {

    private final Context _context;
    private String _label;
    private volatile boolean _needSet;
    private volatile boolean _needReset;

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
            Log.d("BR", "Accel");

            if (_needSet || _needReset) {
                ContentValues cv = intent.getParcelableExtra(LinearAccelerometer.EXTRA_DATA);
                double x = cv.getAsDouble(Linear_Accelerometer_Provider.Linear_Accelerometer_Data.VALUES_0);
                double y = cv.getAsDouble(Linear_Accelerometer_Provider.Linear_Accelerometer_Data.VALUES_1);
                double z = cv.getAsDouble(Linear_Accelerometer_Provider.Linear_Accelerometer_Data.VALUES_2);
                double len = x*x+y*y+z*z;

                Log.d("BR", "Acc "+len);

                if (_needSet && len > 50) {
                    Log.d("BR", "SET");

                    _needSet = false;
                    _needReset = true;
                    SetSensorLabels(_label);

                    MediaPlayer.create(_context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .start();
                }

                if(_needReset && len <10)
                {
                    Stop();

                    MediaPlayer.create(_context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .start();
                }
            }
        }

        if (intent.getAction() == GlobalAccel.ACTION_NEW_DATA) {

        }

        if (intent.getAction() == Rotation.ACTION_AWARE_ROTATION) {

        }

        if (intent.getAction() == Gravity.ACTION_AWARE_GRAVITY) {

        }

    }
}
