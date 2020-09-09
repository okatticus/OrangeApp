package com.example.android.orange;

/**
 * Created by Apoorva on 6/21/2018.
 */

public class Scans {
    public static final String TABLE_NAME = "SCANS";
    public static final String COL_USERID = "UserID";
    public static final String COL_SCAN = "Data";
    public static final String COL_DATE = "Date";
    public static final String COL_TYPE ="TypeBar";

    public static final String CREATE_TABLE = "CREATE TABLE " + Scans.TABLE_NAME + "( " + Scans.COL_USERID + " INTEGER NOT NULL," +
             Scans.COL_SCAN + " TEXT," + Scans.COL_DATE + " TEXT," + Scans.COL_TYPE + " TEXT,"+
            " FOREIGN KEY ( " + COL_USERID + ")REFERENCES " + User.TABLE_NAME + " (" + User.COL_UID + "))";
    private String date, scan,type;
    private long userId;

    public Scans() {
    }

    public Scans(long userID, String scan, String date,String type) {
        this.userId = userID;
        this.scan = scan;
        this.date = date;
        this.type=type;
    }

    public String getScan() {
        return scan;
    }

    public String getDate() {
        return date;
    }
}

