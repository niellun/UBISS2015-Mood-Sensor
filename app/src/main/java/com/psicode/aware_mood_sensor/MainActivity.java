package com.psicode.aware_mood_sensor;

import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.aware.Accelerometer;
import com.aware.Applications;
import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.Locations;
import com.aware.ESM;
import com.aware.providers.Accelerometer_Provider;
import com.aware.providers.Applications_Provider;
import com.aware.ui.ESM_Queue;
import com.aware.providers.Communication_Provider;
import com.aware.providers.Keyboard_Provider;
import com.aware.providers.Locations_Provider;


public class MainActivity extends ActionBarActivity {

    private AccelBroadcastReceiver _receiver;
    private AccelerometerContentObserver _observer;
    private LocationBroadcastReceiver _receiverlocation;
    private LocationContentObserver _observerlocation;
    private ComContentObserver _observercommunication;
    private AppContentObserver _observerapplication;
    private KeyContentObserver _observerkeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView)findViewById(R.id.main);
        TextView latitude = (TextView)findViewById(R.id.latitude);

        //Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ACCELEROMETER, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_LOCATION_GPS, true);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ESM, true);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_COMMUNICATION_EVENTS, true);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_CALLS, true);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_APPLICATIONS, true);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_NOTIFICATIONS, true);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_KEYBOARD, true);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.FREQUENCY_LOCATION_GPS, 20000);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.MIN_LOCATION_GPS_ACCURACY, 150);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.LOCATION_EXPIRATION_TIME, 300);

        //Define the ESM to be displayed
        String esmString = "[{'esm': {\n" +
                "'esm_type': 4,\n" +
                "'esm_title': 'ESM Likert',\n" +
                "'esm_instructions': 'How happy are you?',\n" +
                "'esm_likert_max': 5,\n" +
                "'esm_likert_max_label': 'Sad',\n" +
                "'esm_likert_min_label': 'Happy',\n" +
                "'esm_likert_step': 1,\n" +
                "'esm_submit': 'OK',\n" +
                "'esm_expiration_threashold': 60,\n" +
                "'esm_trigger': 'AWARE Tester'\n" +
                "}}," +
                "{'esm': {\n" +
                "'esm_type': 4,\n" +
                "'esm_title': 'ESM Likert',\n" +
                "'esm_instructions': 'How calm are you?',\n" +
                "'esm_likert_max': 5,\n" +
                "'esm_likert_max_label': 'Angry',\n" +
                "'esm_likert_min_label': 'Calm',\n" +
                "'esm_likert_step': 1,\n" +
                "'esm_submit': 'OK',\n" +
                "'esm_expiration_threashold': 60,\n" +
                "'esm_trigger': 'AWARE Tester'\n" +
                "}},{'esm': {\n" +
                "'esm_type': 4,\n" +
                "'esm_title': 'ESM Likert',\n" +
                "'esm_instructions': 'How in Love are you?',\n" +
                "'esm_likert_max': 5,\n" +
                "'esm_likert_max_label': 'Lonely',\n" +
                "'esm_likert_min_label': 'In love',\n" +
                "'esm_likert_step': 1,\n" +
                "'esm_submit': 'OK',\n" +
                "'esm_expiration_threashold': 60,\n" +
                "'esm_trigger': 'AWARE Tester'\n" +
                "}}]";

        //Queue the ESM to be displayed when possible
        Intent esm = new Intent(ESM.ACTION_AWARE_QUEUE_ESM);
        esm.putExtra(ESM.EXTRA_ESM, esmString);
        sendBroadcast(esm);


       /* _receiver = new AccelBroadcastReceiver(tv);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Accelerometer.ACTION_AWARE_ACCELEROMETER);
        registerReceiver(_receiver, filter);

        _receiverlocation = new LocationBroadcastReceiver(latitude);
        IntentFilter filter_location = new IntentFilter();
        filter_location.addAction(Locations.ACTION_AWARE_LOCATIONS);
        filter_location.addAction(Locations.ACTION_AWARE_GPS_LOCATION_ENABLED);
        registerReceiver(_receiverlocation, filter_location);

        _observer = new AccelerometerContentObserver(new Handler(), getApplicationContext());
        getContentResolver().registerContentObserver(Accelerometer_Provider.Accelerometer_Data.CONTENT_URI, true, _observer);

        _observerlocation = new LocationContentObserver(new Handler(), getApplicationContext());
        getContentResolver().registerContentObserver(Locations_Provider.Locations_Data.CONTENT_URI, true, _observerlocation);

        _observercommunication = new ComContentObserver(new Handler(), getApplicationContext());
        getContentResolver().registerContentObserver(Communication_Provider.Calls_Data.CONTENT_URI, true, _observercommunication);

        _observerapplication = new AppContentObserver(new Handler(), getApplicationContext());
        getContentResolver().registerContentObserver(Applications_Provider.Applications_Notifications.CONTENT_URI, true, _observerapplication);

        _observerkeyboard = new KeyContentObserver(new Handler(), getApplicationContext());
        getContentResolver().registerContentObserver(Keyboard_Provider.Keyboard_Data.CONTENT_URI, true, _observerkeyboard);

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

        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ACCELEROMETER, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ESM, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_LOCATION_GPS, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_COMMUNICATION_EVENTS, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_CALLS, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_APPLICATIONS, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_NOTIFICATIONS, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_KEYBOARD, false);
        sendBroadcast(new Intent(Aware.ACTION_AWARE_REFRESH));
        unregisterReceiver(_receiver);
        unregisterReceiver(_receiverlocation);
        getContentResolver().unregisterContentObserver(_observer);
        getContentResolver().unregisterContentObserver(_observerlocation);
        getContentResolver().unregisterContentObserver(_observercommunication);
        getContentResolver().unregisterContentObserver(_observerapplication);
        getContentResolver().unregisterContentObserver(_observerkeyboard);
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
