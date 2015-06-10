package com.psicode.aware_mood_sensor;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import com.aware.providers.Communication_Provider;

/**
 * Created by KAI on 2015/6/10.
 */
public class ComContentObserver extends ContentObserver {
    private Context _context;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public ComContentObserver(Handler handler, Context context) {
        super(handler);
        _context = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        Log.d("ACO", "OnChange");

        Cursor call_data = _context.getContentResolver()
                .query(Communication_Provider.Calls_Data.CONTENT_URI,
                        new String[]{Communication_Provider.Calls_Data.TYPE},
                        Communication_Provider.Calls_Data.TIMESTAMP + " > " + (System.currentTimeMillis()-(10*3600*1000)),
                        null,
                        Communication_Provider.Calls_Data.TIMESTAMP + " DESC LIMIT 100");


        if(call_data==null)
            return;

        if(call_data.moveToFirst())
            do{
                double call = call_data.getDouble(call_data.getColumnIndex(Communication_Provider.Calls_Data.TYPE));
                Log.d("call", "Call Type: " + call);

            }while(call_data.moveToNext());
        call_data.close();
    }
}
