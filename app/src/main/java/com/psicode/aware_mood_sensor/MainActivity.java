package com.psicode.aware_mood_sensor;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aware.Accelerometer;
import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.Gravity;
import com.aware.Gyroscope;
import com.aware.LinearAccelerometer;
import com.aware.Rotation;
import com.aware.providers.Accelerometer_Provider;
import com.aware.providers.Gravity_Provider;
import com.aware.providers.Gyroscope_Provider;
import com.aware.providers.Linear_Accelerometer_Provider;
import com.aware.providers.Rotation_Provider;

public class MainActivity extends ActionBarActivity {

    private AccelBroadcastReceiver _receiver;
    private AccelerometerContentObserver _observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView)findViewById(R.id.main);
        Button switcher_on = (Button)findViewById(R.id.button);
        Button switcher_off = (Button)findViewById(R.id.button2);

        switcher_on.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_LINEAR_ACCELEROMETER, true);
                Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_GYROSCOPE, true);
                Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ROTATION, true);
                Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_GRAVITY, true);

                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), notification);
                mp.start();
                Toast.makeText(getApplicationContext(), "Sensors On!", Toast.LENGTH_SHORT).show();

                sendBroadcast(new Intent(Aware.ACTION_AWARE_REFRESH));
            }
        });

        switcher_off.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_LINEAR_ACCELEROMETER, false);
                Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_GYROSCOPE, false);
                Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ROTATION, false);
                Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_GRAVITY, false);
                Toast.makeText(getApplicationContext(), "Sensors Off!", Toast.LENGTH_SHORT).show();

                sendBroadcast(new Intent(Aware.ACTION_AWARE_REFRESH));
            }
        });

        _receiver = new AccelBroadcastReceiver(tv);
        IntentFilter filter = new IntentFilter();
        filter.addAction(LinearAccelerometer.ACTION_AWARE_LINEAR_ACCELEROMETER);
        filter.addAction(Gyroscope.ACTION_AWARE_GYROSCOPE);
        filter.addAction(Rotation.ACTION_AWARE_ROTATION);
        filter.addAction(Gravity.ACTION_AWARE_GRAVITY);
        registerReceiver(_receiver, filter);

        _observer = new AccelerometerContentObserver(new Handler(), getApplicationContext());
        getContentResolver().registerContentObserver(Linear_Accelerometer_Provider.Linear_Accelerometer_Data.CONTENT_URI, true, _observer);
        getContentResolver().registerContentObserver(Gyroscope_Provider.Gyroscope_Data.CONTENT_URI, true, _observer);
        getContentResolver().registerContentObserver(Rotation_Provider.Rotation_Data.CONTENT_URI, true, _observer);
        getContentResolver().registerContentObserver(Gravity_Provider.Gravity_Data.CONTENT_URI, true, _observer);

        sendBroadcast(new Intent(Aware.ACTION_AWARE_REFRESH));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //       Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ACCELEROMETER, false);
        sendBroadcast(new Intent(Aware.ACTION_AWARE_REFRESH));
        unregisterReceiver(_receiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
