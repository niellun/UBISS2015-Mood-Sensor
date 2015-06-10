package com.psicode.aware_mood_sensor;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;

import com.aware.providers.Accelerometer_Provider;
import com.aware.providers.Locations_Provider;

/**
 * Created by KAI on 2015/6/9.
 */
public class LocationContentObserver extends ContentObserver {
    private Context _context;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public LocationContentObserver(Handler handler, Context context) {
        super(handler);
        _context = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        Log.d("loc", "OnChange");

        Cursor location_data = _context.getContentResolver()
                .query(Locations_Provider.Locations_Data.CONTENT_URI,
                        new String[]{Locations_Provider.Locations_Data.LATITUDE,
                                Locations_Provider.Locations_Data.LONGITUDE},
                        Locations_Provider.Locations_Data.TIMESTAMP + " > " + (System.currentTimeMillis() - (10 * 3600 * 1000)),
                        null,
                        Locations_Provider.Locations_Data.TIMESTAMP + " DESC LIMIT 100");

        if(location_data == null)
            return;

        if(location_data.moveToFirst())
            do{
                double latitude = location_data.getDouble(location_data.getColumnIndex(Locations_Provider.Locations_Data.LATITUDE));
                double longitude = location_data.getDouble(location_data.getColumnIndex(Locations_Provider.Locations_Data.LONGITUDE));
                Log.d("location", "Latitude=" + latitude);
                Log.d("location", "Longitude=" + longitude);
            }while(location_data.moveToNext());
        location_data.close();
    }
}
