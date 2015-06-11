package com.psicode.aware_mood_sensor;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.Gravity;
import com.aware.LinearAccelerometer;
import com.aware.Rotation;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends ActionBarActivity {

    private final SensorBroadcastReceiver _broadcastReceiver;
    private final ScheduledExecutorService _scheduler;

    private GlobalAccel _globalAccelerometer;
    private Button _btnSwithOn;
    private Integer _counter;

    public MainActivity() {
        _scheduler = Executors.newSingleThreadScheduledExecutor();
        _counter = 0;

        // Set broadcast receiver
        _broadcastReceiver = new SensorBroadcastReceiver(getApplicationContext());
        IntentFilter filter = new IntentFilter();
        filter.addAction(LinearAccelerometer.ACTION_AWARE_LINEAR_ACCELEROMETER);
        filter.addAction(Rotation.ACTION_AWARE_ROTATION);
        filter.addAction(Gravity.ACTION_AWARE_GRAVITY);
        filter.addAction(GlobalAccel.ACTION_NEW_DATA);
        registerReceiver(_broadcastReceiver, filter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _btnSwithOn = (Button) findViewById(R.id.button);

        // Turn sensors on
        _globalAccelerometer = new GlobalAccel(getApplicationContext());
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_LINEAR_ACCELEROMETER, true);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_GYROSCOPE, true);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ROTATION, true);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_GRAVITY, true);

        // Update aware
        sendBroadcast(new Intent(Aware.ACTION_AWARE_REFRESH));
    }

    public void OnStartClick(View view) {
        _btnSwithOn.setText("THROW AWAY");

        _broadcastReceiver.Start(_counter.toString());

        _scheduler.schedule(OnStop, 5, TimeUnit.SECONDS);
    }

    Runnable OnStop = new Runnable() {

        public void run() {
            _broadcastReceiver.Stop();

            runOnUiThread(new Runnable() {
                public void run() {
                    // Update UI elements
                    _btnSwithOn.setText("TURN IT ON");
                }
            });
        }
    };

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
        unregisterReceiver(_broadcastReceiver);
        _globalAccelerometer.Destroy();
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
