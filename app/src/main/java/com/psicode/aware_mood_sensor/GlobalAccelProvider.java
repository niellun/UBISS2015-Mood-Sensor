package com.psicode.aware_mood_sensor;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;
import android.util.Log;

import com.aware.Aware;
import com.aware.utils.DatabaseHelper;

import java.util.HashMap;

public class GlobalAccelProvider extends ContentProvider {

    public static final int DATABASE_VERSION = 1;

    /**
     * Provider authority: com.aware.plugin.lux_meter.provider.lux_meter
     */

    public static String AUTHORITY = "com.aware.sensor.global_accelerometer";

    public static final String DATABASE_NAME = Environment.getExternalStorageDirectory() + "/AWARE/sensor_global_accelerometer.db";

    private static final int GACELLEROMETER = 1;
    private static final int GACELLEROMETER_ID = 2;

    public static final String[] DATABASE_TABLES = {
            "sensor_global_accelerometer"
    };

    public static final String[] TABLES_FIELDS = {
            GlobalAccel_Data._ID + " integer primary key autoincrement," +
                    GlobalAccel_Data.TIMESTAMP + " real default 0," +
                    GlobalAccel_Data.DEVICE_ID + " text default ''," +
                    GlobalAccel_Data.VALUE_X + " real default 0," +
                    GlobalAccel_Data.VALUE_Y + " real default 0," +
                    GlobalAccel_Data.VALUE_Z + " real default 0," +
                    GlobalAccel_Data.LABLE + " text default ''," +
                    "UNIQUE("+ GlobalAccel_Data.TIMESTAMP+","+ GlobalAccel_Data.DEVICE_ID+")"
    };

    public static final class GlobalAccel_Data implements BaseColumns {
        private GlobalAccel_Data(){};

        public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/plugin_indoorsensor");
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.aware.plugin.indoorsensor";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.aware.plugin.indoorsensor";

        public static final String _ID = "id";
        public static final String TIMESTAMP = "timestamp";
        public static final String DEVICE_ID = "device_id";
        public static final String VALUE_X = "x_value";
        public static final String VALUE_Y = "y_value";
        public static final String VALUE_Z = "z_value";
        public static final String LABLE = "lable";
    }

    private static UriMatcher URIMatcher;
    private static HashMap<String, String> databaseMap;
    private static DatabaseHelper databaseHelper;
    private static SQLiteDatabase database;

    @Override
    public boolean onCreate() {

        AUTHORITY = getContext().getPackageName() + ".provider.indoorsensor";

        URIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        URIMatcher.addURI(AUTHORITY, DATABASE_TABLES[0], GACELLEROMETER);
        URIMatcher.addURI(AUTHORITY, DATABASE_TABLES[0] + "/#", GACELLEROMETER_ID);

        databaseMap = new HashMap<String, String>();
        databaseMap.put(GlobalAccel_Data._ID, GlobalAccel_Data._ID);
        databaseMap.put(GlobalAccel_Data.TIMESTAMP, GlobalAccel_Data.TIMESTAMP);
        databaseMap.put(GlobalAccel_Data.DEVICE_ID, GlobalAccel_Data.DEVICE_ID);
        databaseMap.put(GlobalAccel_Data.VALUE_X, GlobalAccel_Data.VALUE_X);
        databaseMap.put(GlobalAccel_Data.VALUE_Y, GlobalAccel_Data.VALUE_Y);
        databaseMap.put(GlobalAccel_Data.VALUE_Z, GlobalAccel_Data.VALUE_Z);
        databaseMap.put(GlobalAccel_Data.LABLE, GlobalAccel_Data.LABLE);

        return true;
    }

    private boolean initializeDB() {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION, DATABASE_TABLES, TABLES_FIELDS );
        }
        if( databaseHelper != null && ( database == null || ! database.isOpen() )) {
            database = databaseHelper.getWritableDatabase();
        }
        return( database != null && databaseHelper != null);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if( ! initializeDB() ) {
            Log.w(AUTHORITY, "Database unavailable...");
            return 0;
        }

        int count = 0;
        switch (URIMatcher.match(uri)) {
            case GACELLEROMETER:
                count = database.delete(DATABASE_TABLES[0], selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (URIMatcher.match(uri)) {
            case GACELLEROMETER:
                return GlobalAccel_Data.CONTENT_TYPE;
            case GACELLEROMETER_ID:
                return GlobalAccel_Data.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if( ! initializeDB() ) {
            Log.w(AUTHORITY,"Database unavailable...");
            return null;
        }

        ContentValues values = (initialValues != null) ? new ContentValues(
                initialValues) : new ContentValues();

        switch (URIMatcher.match(uri)) {
            case GACELLEROMETER:
                long weather_id = database.insert(DATABASE_TABLES[0], GlobalAccel_Data.DEVICE_ID, values);

                if (weather_id > 0) {
                    Uri new_uri = ContentUris.withAppendedId(
                            GlobalAccel_Data.CONTENT_URI,
                            weather_id);
                    getContext().getContentResolver().notifyChange(new_uri,
                            null);
                    return new_uri;
                }
                throw new SQLException("Failed to insert row into " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI "+URIMatcher.match(uri) + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        if( ! initializeDB() ) {
            Log.w(AUTHORITY,"Database unavailable...");
            return null;
        }

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (URIMatcher.match(uri)) {
            case GACELLEROMETER:
                qb.setTables(DATABASE_TABLES[0]);
                qb.setProjectionMap(databaseMap);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        try {
            Cursor c = qb.query(database, projection, selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;
        } catch (IllegalStateException e) {
            if (Aware.DEBUG)
                Log.e(Aware.TAG, e.getMessage());

            return null;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        if( ! initializeDB() ) {
            Log.w(AUTHORITY,"Database unavailable...");
            return 0;
        }

        int count = 0;
        switch (URIMatcher.match(uri)) {
            case GACELLEROMETER:
                count = database.update(DATABASE_TABLES[0], values, selection,
                        selectionArgs);
                break;
            default:

                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}