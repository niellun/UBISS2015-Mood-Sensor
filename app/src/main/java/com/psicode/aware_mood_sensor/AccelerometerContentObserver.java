package com.psicode.aware_mood_sensor;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;

import com.aware.providers.Accelerometer_Provider;

/**
 * Created by niellune on 09.06.15.
 */
public class AccelerometerContentObserver extends ContentObserver {

    private Context _context;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public AccelerometerContentObserver(Handler handler, Context context) {
        super(handler);
        _context = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        Log.d("ACO", "OnChange");


        Cursor raw_data = _context.getContentResolver()
                .query(Accelerometer_Provider.Accelerometer_Data.CONTENT_URI,
                        new String[]{Accelerometer_Provider.Accelerometer_Data.VALUES_0,
                                Accelerometer_Provider.Accelerometer_Data.VALUES_1,
                                Accelerometer_Provider.Accelerometer_Data.VALUES_2},
                        Accelerometer_Provider.Accelerometer_Data.TIMESTAMP + " > " + (System.currentTimeMillis()-(10*3600*1000)),
                        null,
                        Accelerometer_Provider.Accelerometer_Data.TIMESTAMP + " DESC LIMIT 100");

        if(raw_data==null)
            return;

        if(raw_data.moveToFirst())
            do{
                double x = raw_data.getDouble(raw_data.getColumnIndex(Accelerometer_Provider.Accelerometer_Data.VALUES_0));
                double y = raw_data.getDouble(raw_data.getColumnIndex(Accelerometer_Provider.Accelerometer_Data.VALUES_1));
                double z = raw_data.getDouble(raw_data.getColumnIndex(Accelerometer_Provider.Accelerometer_Data.VALUES_2));

                double len = Math.sqrt(x*x+y*y+z*z);
                //Log.d("ACO", "content " + len);

            }while(raw_data.moveToNext());

        raw_data.close();
    }
}
