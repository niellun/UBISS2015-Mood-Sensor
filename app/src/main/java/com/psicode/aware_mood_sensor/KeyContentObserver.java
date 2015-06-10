package com.psicode.aware_mood_sensor;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;

import com.aware.providers.Applications_Provider;
import com.aware.providers.Keyboard_Provider;

/**
 * Created by KAI on 2015/6/10.
 */
public class KeyContentObserver extends ContentObserver{
    private Context _context;

    public KeyContentObserver(Handler handler, Context context) {
        super(handler);
        _context = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        Log.d("ACO", "OnChange");

        Cursor key_data = _context.getContentResolver()
                .query(Keyboard_Provider.Keyboard_Data.CONTENT_URI,
                        new String[]{Keyboard_Provider.Keyboard_Data.PACKAGE_NAME},
                        Keyboard_Provider.Keyboard_Data.TIMESTAMP + " > " + (System.currentTimeMillis()-(10*3600*1000)),
                        null,
                        Keyboard_Provider.Keyboard_Data.TIMESTAMP + " DESC LIMIT 100");

        if(key_data==null)
            return;

        if(key_data.moveToFirst())
            do{
                String key = key_data.getString(key_data.getColumnIndex(Keyboard_Provider.Keyboard_Data.PACKAGE_NAME));
                Log.d("key", "App Name: " + key);

            }while(key_data.moveToNext());
        key_data.close();
    }
}
