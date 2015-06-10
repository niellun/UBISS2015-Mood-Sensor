package com.psicode.aware_mood_sensor;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;

import com.aware.providers.Applications_Provider;

/**
 * Created by KAI on 2015/6/10.
 */
public class AppContentObserver extends ContentObserver {
    private Context _context;

    public AppContentObserver(Handler handler, Context context) {
        super(handler);
        _context = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        Log.d("ACO", "OnChange");

        Cursor app_data = _context.getContentResolver()
                .query(Applications_Provider.Applications_Notifications.CONTENT_URI,
                        new String[]{Applications_Provider.Applications_Notifications.PACKAGE_NAME},
                        Applications_Provider.Applications_Notifications.TIMESTAMP + " > " + (System.currentTimeMillis()-(10*3600*1000)),
                        null,
                        Applications_Provider.Applications_Notifications.TIMESTAMP + " DESC LIMIT 100");


        if(app_data==null)
            return;

        if(app_data.moveToFirst())
            do{
                String app = app_data.getString(app_data.getColumnIndex(Applications_Provider.Applications_Notifications.PACKAGE_NAME));
                Log.d("app", "App Name: " + app);

            }while(app_data.moveToNext());
        app_data.close();
    }
}
