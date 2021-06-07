package org.db.employeedatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static org.db.employeedatabase.Constants.ADDRESS;
import static org.db.employeedatabase.Constants.AGE;
import static org.db.employeedatabase.Constants.NAME;
import static org.db.employeedatabase.Constants.POSITION;
import static org.db.employeedatabase.Constants.TABLE_NAME;

public class EventsData extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "events.db";
    private static final int DATABASE_VERSION = 1;

    public EventsData(Context ctx) {

        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT NOT NULL,"
                + ADDRESS + " TEXT NOT NULL," + AGE + " INTEGER," + POSITION + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
