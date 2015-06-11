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
    private boolean _needSet;

    public SensorBroadcastReceiver(Context context) {
        _context = context;
        _label = "";
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
        _label = label;
        _needSet = true;
    }

    public void Stop() {
        _needSet = false;
        SetSensorLabels("");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == LinearAccelerometer.ACTION_AWARE_LINEAR_LABEL) {

            if (_needSet) {
                double x = intent.getDoubleExtra(Linear_Accelerometer_Provider.Linear_Accelerometer_Data.VALUES_0, 0);
                double y = intent.getDoubleExtra(Linear_Accelerometer_Provider.Linear_Accelerometer_Data.VALUES_1, 0);
                double z = intent.getDoubleExtra(Linear_Accelerometer_Provider.Linear_Accelerometer_Data.VALUES_2, 0);
                double len = x * x + y * y + z * z;

                if (len > 50) {
                    _needSet = false;
                    SetSensorLabels(_label);

                    MediaPlayer.create(_context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .start();
                }
            }
        }

        if (intent.getAction() == Gyroscope.ACTION_AWARE_GYROSCOPE_LABEL) {

        }

        if (intent.getAction() == Rotation.ACTION_AWARE_ROTATION_LABEL) {

        }

        if (intent.getAction() == Gravity.ACTION_AWARE_GRAVITY_LABEL) {

        }

    }
}
