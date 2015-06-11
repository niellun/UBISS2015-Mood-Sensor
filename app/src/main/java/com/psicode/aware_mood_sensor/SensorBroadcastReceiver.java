package com.psicode.aware_mood_sensor;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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

   private TextView _tv;


    public SensorBroadcastReceiver() {

}

    @Override
    public void onReceive(Context context, Intent intent) {

        /*
        Intent Acc_fall = new Intent(LinearAccelerometer.ACTION_AWARE_LINEAR_LABEL);
        Acc_fall.putExtra(LinearAccelerometer.EXTRA_LABEL, "Acceleration_fall_down");
        context.sendBroadcast(Acc_fall);

        Intent gyo_fall = new Intent(Gyroscope.ACTION_AWARE_GYROSCOPE_LABEL);
        gyo_fall.putExtra(Gyroscope.EXTRA_LABEL, "Gyroscope_fall_down");
        context.sendBroadcast(gyo_fall);

        Intent rota_fall = new Intent(Rotation.ACTION_AWARE_ROTATION_LABEL);
        rota_fall.putExtra(Rotation.EXTRA_LABEL, "Rotation_fall_down");
        context.sendBroadcast(rota_fall);

        Intent gra_fall = new Intent(Gravity.ACTION_AWARE_GRAVITY_LABEL);
        gra_fall.putExtra(Gravity.EXTRA_LABEL, "Gravity_fall_down");
        context.sendBroadcast(gra_fall);

        */

//        ContentValues cv = intent.getParcelableExtra(LinearAccelerometer.EXTRA_DATA);
//
//        double x = cv.getAsDouble(Linear_Accelerometer_Provider.Linear_Accelerometer_Data.VALUES_0);
//        double y = cv.getAsDouble(Linear_Accelerometer_Provider.Linear_Accelerometer_Data.VALUES_1);
//        double z = cv.getAsDouble(Linear_Accelerometer_Provider.Linear_Accelerometer_Data.VALUES_2);
//
//        double len = Math.sqrt(x * x + y * y + z * z);
//        Log.d("BR", "Acceleration " + len);
//        _tv.setText(Double.toString(len));

       // Log.d("BR", cv.toString());
    }
}
