package com.example.android.orange;

/**
 * Created by Apoorva on 5/30/2018.
 */

public class User {
    public static final String TABLE_NAME = "USERDATA1";
    public static final String COL_UID = "uid";
    public static final String COL_NAME = "name";
    public static final String COL_LAST = "lastName";
    public static final String COL_MAIL = "mail";
    public static final String COL_PASS = "pass";
    public static final String COL_TIME = "timestamp";
    public static final String COL_TYPE = "usertype";
    public static final String COL_DOB = "dob";

    //create a table for userdata
    public static final String CREATE_TABLE = "CREATE TABLE " + User.TABLE_NAME + "( " + User.COL_UID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + User.COL_NAME +
            " TEXT," + User.COL_LAST + " TEXT, " + User.COL_MAIL + " TEXT," + User.COL_PASS + " TEXT," + User.COL_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP ,"
            + User.COL_TYPE + " TEXT, " + User.COL_DOB + " TEXT)";

    private String name, lastname, mail, pass, timestamp, usertype, dob;
    long userID;

    public User() {
    }

    public User(long userID, String name, String lastname, String mail, String pass, String time, String usertype, String dob) {
        this.userID = userID;
        this.name = name;
        this.lastname = lastname;
        this.mail = mail;
        this.pass = pass;
        this.timestamp = time;
        this.usertype = usertype;
        this.dob = dob;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastname;
    }

    public String getPAss() {
        return pass;
    }

    public String getUserType() {
        return usertype;
    }

    public long getUserId() {
        return userID;
    }

    public String getMail() {
        return mail;
    }
}


