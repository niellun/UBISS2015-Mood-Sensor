package com.psicode.aware_mood_sensor;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.Gravity;
import com.aware.Gyroscope;
import com.aware.LinearAccelerometer;
import com.aware.Rotation;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends ActionBarActivity {

    private SensorBroadcastReceiver mBroadcastReceiver;
    private GlobalAccel mGlobalAceelerometer;
    private static final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
    private static Button switcher_on;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Turn sensors on
        mGlobalAceelerometer = new GlobalAccel(getApplicationContext());
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_LINEAR_ACCELEROMETER, true);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_GYROSCOPE, true);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ROTATION, true);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_GRAVITY, true);

        mBroadcastReceiver = new SensorBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(LinearAccelerometer.ACTION_AWARE_LINEAR_ACCELEROMETER);
        filter.addAction(Rotation.ACTION_AWARE_ROTATION);
        filter.addAction(Gravity.ACTION_AWARE_GRAVITY);
        filter.addAction(GlobalAccel.ACTION_NEW_DATA);
        registerReceiver(mBroadcastReceiver, filter);

        sendBroadcast(new Intent(Aware.ACTION_AWARE_REFRESH));






        switcher_on = (Button) findViewById(R.id.button);

        switcher_on.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // TURN LABLE ON

                Log.d("BTN", "ON");

                switcher_on.setText("THROW AWAY");
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), notification);
                mp.start();
                Toast.makeText(getApplicationContext(), "Throw it in 3 seconds!", Toast.LENGTH_SHORT).show();

                Intent Acc_fall = new Intent(LinearAccelerometer.ACTION_AWARE_LINEAR_LABEL);
                Acc_fall.putExtra(LinearAccelerometer.EXTRA_LABEL, "FALL");
                getApplicationContext().sendBroadcast(Acc_fall);

                Intent gyo_fall = new Intent(Gyroscope.ACTION_AWARE_GYROSCOPE_LABEL);
                gyo_fall.putExtra(Gyroscope.EXTRA_LABEL, "FALL");
                getApplicationContext().sendBroadcast(gyo_fall);

                Intent rota_fall = new Intent(Rotation.ACTION_AWARE_ROTATION_LABEL);
                rota_fall.putExtra(Rotation.EXTRA_LABEL, "FALL");
                getApplicationContext().sendBroadcast(rota_fall);

                Intent gra_fall = new Intent(Gravity.ACTION_AWARE_GRAVITY_LABEL);
                gra_fall.putExtra(Gravity.EXTRA_LABEL, "FALL");
                getApplicationContext().sendBroadcast(gra_fall);

                Runnable task = new Runnable() {

                    public void run() {
                        // TURN LABLE OFF

                        Log.d("BTN", "OFF");

                        runOnUiThread(new Runnable() {
                            public void run() {
                                // Update UI elements
                                switcher_on.setText("TURN IT ON");
                            }
                        });

                        Intent Acc_fall = new Intent(LinearAccelerometer.ACTION_AWARE_LINEAR_LABEL);
                        Acc_fall.putExtra(LinearAccelerometer.EXTRA_LABEL, "");
                        getApplicationContext().sendBroadcast(Acc_fall);

                        Intent gyo_fall = new Intent(Gyroscope.ACTION_AWARE_GYROSCOPE_LABEL);
                        gyo_fall.putExtra(Gyroscope.EXTRA_LABEL, "");
                        getApplicationContext().sendBroadcast(gyo_fall);

                        Intent rota_fall = new Intent(Rotation.ACTION_AWARE_ROTATION_LABEL);
                        rota_fall.putExtra(Rotation.EXTRA_LABEL, "");
                        getApplicationContext().sendBroadcast(rota_fall);

                        Intent gra_fall = new Intent(Gravity.ACTION_AWARE_GRAVITY_LABEL);
                        gra_fall.putExtra(Gravity.EXTRA_LABEL, "");
                        getApplicationContext().sendBroadcast(gra_fall);



                    }
                };

                worker.schedule(task, 5, TimeUnit.SECONDS);

                /*
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
                */
            }
        });

        gaccel = new GlobalAccel(getApplicationContext());
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_LINEAR_ACCELEROMETER, true);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_GYROSCOPE, true);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ROTATION, true);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_GRAVITY, true);

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
        unregisterReceiver(mBroadcastReceiver);
        mGlobalAceelerometer.Destroy();
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
