package com.psicode.aware_mood_sensor;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.aware.Locations;
import com.aware.providers.Locations_Provider;

/**
 * Created by KAI on 2015/6/9.
 */
public class LocationBroadcastReceiver extends BroadcastReceiver {
    private TextView _latitude;


    public LocationBroadcastReceiver(TextView latitude) {
        _latitude = latitude;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        ContentValues lati = new ContentValues();
 //       double latitude = lati.getAsDouble(Locations_Provider.Locations_Data.LATITUDE);
 //       _latitude.setText(Double.toString(latitude));

        // Log.d("BR", cv.toString());
    }
}
