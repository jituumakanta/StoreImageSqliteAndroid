package com.coderzheaven.storeimage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class database {

    private static final String DATABASE_NAME = "Images.db";
    private static final int DATABASE_VERSION = 1;
    private static final String IMAGES_TABLE = "ImagesTable";

    public static final String IMAGE_ID = "id";
    private static final String KEY_COLUMN_PRODUCTNAME = "ProductNameTable";
    public static final String IMAGE = "image";


    private final Context mContext;

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;


    private static final String CREATE_IMAGES_TABLE = "CREATE TABLE " + IMAGES_TABLE + " (" + IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_COLUMN_PRODUCTNAME + " TEXT," +
            IMAGE + " BLOB );";


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_IMAGES_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_IMAGES_TABLE);
            onCreate(db);
        }
    }

    public void Reset() {
        mDbHelper.onUpgrade(this.mDb, 1, 1);
    }

    public database(Context ctx) {
        mContext = ctx;
        mDbHelper = new DatabaseHelper(mContext);
    }

    public database open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    // Insert the image to the Sqlite DB
    public void insertImage(byte[] imageBytes) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_COLUMN_PRODUCTNAME, "fff");
        cv.put(IMAGE, imageBytes);
        mDb.insert(IMAGES_TABLE, null, cv);
    }

    // Get the image from SQLite DB
    // We will just get the last image we just saved for convenience...
    public byte[] retreiveImageFromDB() {
        Cursor cur = mDb.query(true, IMAGES_TABLE, new String[]{IMAGE,},
                null, null, null, null,
                IMAGE_ID + " DESC", "1");
      /*  Cursor cur1 = mDb.query(true, IMAGES_TABLE, new String[]{KEY_COLUMN_PRODUCTNAME,},
                null, null, null, null,
                null, null);
        if (cur1.moveToFirst()) {
            String d = cur.getString(cur.getColumnIndex(KEY_COLUMN_PRODUCTNAME));
            System.out.println("aaa"+d);

        }*/
        if (cur.moveToFirst()) {
            //String d=cur.getString(cur.getColumnIndex(KEY_COLUMN_PRODUCTNAME));

            byte[] blob = cur.getBlob(cur.getColumnIndex(IMAGE));
            cur.close();
            return blob;
        }
        cur.close();
        return null;
    }
}