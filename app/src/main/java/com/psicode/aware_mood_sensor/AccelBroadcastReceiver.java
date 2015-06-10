package com.psicode.aware_mood_sensor;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.aware.Accelerometer;
import com.aware.providers.Accelerometer_Provider;

/**
 * Created by niellune on 09.06.15.
 */
public class AccelBroadcastReceiver extends BroadcastReceiver {

   private TextView _tv;


    public AccelBroadcastReceiver(TextView tv) {
        _tv = tv;
}

    @Override
    public void onReceive(Context context, Intent intent) {
        ContentValues cv = intent.getParcelableExtra(Accelerometer.EXTRA_DATA);


        double x = cv.getAsDouble(Accelerometer_Provider.Accelerometer_Data.VALUES_0);
        double y = cv.getAsDouble(Accelerometer_Provider.Accelerometer_Data.VALUES_1);
        double z = cv.getAsDouble(Accelerometer_Provider.Accelerometer_Data.VALUES_2);

        double len = Math.sqrt(x * x + y * y + z * z);
        Log.d("BR", "Acceleration " + len);
        _tv.setText(Double.toString(len));

       // Log.d("BR", cv.toString());
    }
}
