package com.example.android.orange.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.orange.Scans;
import com.example.android.orange.User;

import java.util.ArrayList;

/**
 * Created by Apoorva on 5/30/2018.
 */

public class Db extends SQLiteOpenHelper {
    private static final String DB_NAME = "USERDATAS";
    private static final int DB_VERSION = 1;
    public Db(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Scans.TABLE_NAME);
        sqLiteDatabase.execSQL(User.CREATE_TABLE);
        sqLiteDatabase.execSQL(Scans.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Scans.TABLE_NAME);
        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public long enterData(String name, String lastName, String email, String pass, String usertype, String dob) {
        //get writable database to enter values
        long rowID = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(User.COL_NAME, name);
        values.put(User.COL_LAST, lastName);
        values.put(User.COL_MAIL, email);
        values.put(User.COL_PASS, pass);
        values.put(User.COL_TYPE, usertype);
        values.put(User.COL_DOB, dob);

        Log.v("Db.java", "Entering data of " + name);
        try {
            rowID = db.insertOrThrow(User.TABLE_NAME, null, values);
            if (rowID <= 0)
                throw new SQLiteConstraintException("Failed to store data");
        } catch (SQLiteConstraintException e) {
            Log.v("Db.java", e.toString());
        } finally {
            db.close();
        }
        return rowID;
    }


    public User getData(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        Cursor cursor = null;
        //Cursors contain the result of a query
        try {
            cursor = db.query(
                    User.TABLE_NAME,
                    null,
                    User.COL_MAIL + "=?",
                    new String[]{email}, null, null, null, null
            );
            cursor.moveToFirst();
            user = new User(
                    cursor.getLong(cursor.getColumnIndex(User.COL_UID)),
                    cursor.getString(cursor.getColumnIndex(User.COL_NAME)),
                    cursor.getString(cursor.getColumnIndex(User.COL_LAST)),
                    cursor.getString(cursor.getColumnIndex(User.COL_MAIL)),
                    cursor.getString(cursor.getColumnIndex(User.COL_PASS)),
                    cursor.getString(cursor.getColumnIndex(User.COL_TIME)),
                    cursor.getString(cursor.getColumnIndex(User.COL_TYPE)),
                    cursor.getString(cursor.getColumnIndex(User.COL_DOB))

            );
            cursor.close();
        } catch (CursorIndexOutOfBoundsException e) {
            Log.d("Db.java", "Content provider is empty");
        }
        db.close();
        return user;
    }

    public ArrayList<String> getAllElements() {

        ArrayList<String>
                list = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + User.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        String mail = cursor.getString(cursor.getColumnIndex(User.COL_MAIL));
                        // could add additional columns here..
                        list.add(mail);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        Log.v("jn", list.toString());
        return list;
    }

    public boolean deleteUser(long temp) {
        SQLiteDatabase db = this.getWritableDatabase();
        Boolean x = db.delete(User.TABLE_NAME, User.COL_UID + " = " + temp, null) > 0;
        return x;
    }

    public boolean deleteScan(long temp) {
        SQLiteDatabase db = this.getWritableDatabase();
        Boolean x = db.delete(Scans.TABLE_NAME, Scans.COL_USERID + " = " + temp, null) > 0;
        return x;
    }


    public long enterScan(Long uid, String scan, String date, String type) {
        //get writable database to enter values
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Scans.COL_USERID, uid);
        values.put(Scans.COL_SCAN, scan);
        values.put(Scans.COL_DATE, date);
        values.put(Scans.COL_TYPE, type);

        long rowID = 0;
        try {
            rowID = db.insertOrThrow(Scans.TABLE_NAME, null, values);
            if (rowID <= 0)
                throw new SQLiteConstraintException("Failed to store data");
        } catch (SQLiteConstraintException e) {
            Log.v("Db.java", e.toString());
        } finally {
            db.close();
        }
        return rowID;
    }

    public boolean checkMail(CharSequence email) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        String m = email.toString();
        ArrayList<String> sc = new ArrayList<String>();
        Cursor cursor = null;
        Boolean X = true;
        //Cursors contain the result of a query
        try {
            cursor = db.query(
                    User.TABLE_NAME,
                    null,
                    User.COL_MAIL + " = ?",
                    new String[]{m}, null, null, null, null
            );
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        String ms = cursor.getString(cursor.getColumnIndex(User.COL_MAIL));
                        // could add additional columns here..
                        if (!ms.equals(null)) {
                            X = false;
                        }
                        sc.add(ms);
                    } while (cursor.moveToNext());
                }
                Log.v("Db-mail", m);
                Log.v("Db.java", X.toString());
            } finally {
                try {
                    cursor.close();
                } catch (Exception ignore) {
                }
            }
        } catch (CursorIndexOutOfBoundsException e) {
            Log.d("Db.java", "Content provider is empty");
        }
        return X;
    }

    public int getCountScans(String uid, String type) {
        int qr = 0, bar = 0;
        ArrayList<String> sc = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.query(
                    Scans.TABLE_NAME,
                    null,
                    Scans.COL_USERID + " = ?",
                    new String[]{uid}, null, null, null, null
            );
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        String scans = cursor.getString(cursor.getColumnIndex(Scans.COL_SCAN));
                        String type_s = cursor.getString(cursor.getColumnIndex(Scans.COL_TYPE));
                        if (type_s.equals("qr")) {
                            qr = qr + 1;
                        } else {
                            bar = bar + 1;
                        }
                        // could add additional columns here..
                        sc.add(scans);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        if (type.equals("qr")) {
            return qr;
        } else return bar;
    }

    public ArrayList<String> getAllScans(String uid, String x) {
        ArrayList<String> sc = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.query(
                    Scans.TABLE_NAME,
                    null,
                    Scans.COL_USERID + " = ?",
                    new String[]{uid}, null, null, null, null
            );
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        String scans = cursor.getString(cursor.getColumnIndex(Scans.COL_SCAN));
                        String typeS = cursor.getString(cursor.getColumnIndex(Scans.COL_TYPE));

                        if (x.equals(typeS)) {
                            sc.add(scans);
                        }
                        // could add additional columns here..
                        //sc.add(scans);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();
                } catch (Exception ignore) {
                }
            }
        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return sc;
    }

    public void adminDel() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Scans.TABLE_NAME, null, null);
        db.delete(User.TABLE_NAME, null, null);
    }

    public String checkforScan(long uid, String scan, String dt) {
        String d = null;
        String i = String.valueOf(uid);
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String q = "SELECT * FROM " + Scans.TABLE_NAME + " WHERE " + Scans.COL_USERID + " = ? " + " and " + Scans.COL_SCAN + "= ?";
            Cursor cursor = db.rawQuery(q, new String[]{i, scan});
            if (cursor != null && cursor.moveToFirst()) {
                Scans scan1 = new Scans(
                        cursor.getInt(cursor.getColumnIndex(Scans.COL_USERID)),
                        cursor.getString(cursor.getColumnIndex(Scans.COL_SCAN)),
                        cursor.getString(cursor.getColumnIndex(Scans.COL_DATE)),
                        cursor.getString(cursor.getColumnIndex(Scans.COL_TYPE))
                );
                d = scan1.getDate();
                cursor.close();
            }
        } finally {
            db.close();
        }
        return d;
    }
}