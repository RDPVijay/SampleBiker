package com.example.lenovo.samplebiker;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "samplebiker.db";
    private static final String TABLE_NAME = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_UNAME = "uname";
    private static final String COLUMN_PASS = "pass";
    private static final String COLUMN_PHNNO = "phnno";
    SQLiteDatabase db;
    private static final String TABLE_CREATE = "create table users (id integer primary key not null , " +
            "name text not null , email text not null , uname text not null , pass text not null, phnno integer(10) not null);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        this.db = db;
    }

    public void insertContact(User c) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from users";
        Cursor cursor = db.rawQuery(query , null);
        int count = cursor.getCount();

        values.put(COLUMN_ID , count);
        values.put(COLUMN_NAME, c.getName());
        values.put(COLUMN_EMAIL, c.getEmail());
        values.put(COLUMN_UNAME, c.getUname());
        values.put(COLUMN_PASS, c.getPass());
        values.put(COLUMN_PHNNO, c.getphnno());


        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public String searchPass(String uname)
    {
        db = this.getReadableDatabase();
        String query = "select uname, pass from "+TABLE_NAME;
        Cursor cursor = db.rawQuery(query , null);
        String a, b;
        b = "not found";
        if(cursor.moveToFirst())
        {
            do{
                a = cursor.getString(0);

                if(a.equals(uname))
                {
                    b = cursor.getString(1);
                    break;
                }
            }
            while(cursor.moveToNext());
        }

        return b;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS "+TABLE_NAME;
        db.execSQL(query);
        this.onCreate(db);
    }

    public String searchEmail(String emailstr) {

        db = this.getReadableDatabase();
        String query = "select email from "+TABLE_NAME;
        Cursor cursor = db.rawQuery(query , null);
        String a, b;
        b = "not found";
        if(cursor.moveToFirst())
        {
            do{
                a = cursor.getString(0);

                if(a.equals(emailstr))
                {
                    b = cursor.getString(0);
                    break;
                }
            }
            while(cursor.moveToNext());
        }

        return b;
    }

    public String searchUnam(String unamestr) {

        db = this.getReadableDatabase();
        String query = "select uname from "+TABLE_NAME;
        Cursor cursor = db.rawQuery(query , null);
        String a, b;
        b = "not found";
        if(cursor.moveToFirst())
        {
            do{
                a = cursor.getString(0);

                if(a.equals(unamestr))
                {
                    b = cursor.getString(0);
                    break;
                }
            }
            while(cursor.moveToNext());
        }

        return b;

    }
}
