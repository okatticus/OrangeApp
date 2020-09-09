package com.example.android.orange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.orange.Database.Db;

import java.util.Locale;

/**
 * Created by Apoorva on 5/29/2018.
 */

public class LoginActivity extends AppCompatActivity {
    final String LOG_TAG = "loginActivity";
    Button b, f;
    EditText t1, t2;
    Db db;
    private User user;
    Locale locale = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        b = (Button) findViewById(R.id.Login);
        f = (Button) findViewById(R.id.Forgot);
        t1 = (EditText) findViewById(R.id.text1);
        t2 = (EditText) findViewById(R.id.text2);
        db = new Db(LoginActivity.this);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == t1.getId()) {
                    t1.setCursorVisible(true);
                }
            }
        });


        b.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     String mail = t1.getText().toString().trim();
                                     CharSequence res = mail;
                                     String pass = t2.getText().toString();
                                     if (isEmailValid(res)) {
                                         t1.setError(null);
                                         user = db.getData(mail);
                                         if (user == null) {
                                              AlertDialog ad = new AlertDialog.Builder(
                                                     LoginActivity.this).create();
                                             ad.setTitle("Error");
                                             ad.setMessage("E-mail or password incorrect.");
                                             ad.setIcon(R.drawable.user_error);
                                             ad.show();
                                         } else {
                                             String username = user.getName();
                                             String type = user.getUserType();
                                             long userId = user.getUserId();
                                             String lastName = user.getLastName();
                                             Log.v("Id in Login", String.valueOf(userId));
                                             Log.v("Password", pass);
                                             if (pass.equals(user.getPAss())) {
                                                 Intent i = new Intent(
                                                         LoginActivity.this, UserActivity.class);
                                                 i.putExtra("username", username);
                                                 i.putExtra("usertype", type);
                                                 i.putExtra("userId", Long.toString(userId));
                                                 i.putExtra("lastName", lastName);
                                                 i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                 startActivity(i);
                                             } else
                                                 Toast.makeText(getBaseContext(), "Invalid Password!"
                                                         , Toast.LENGTH_SHORT).show();
                                         }
                                     } else {
                                         t1.setError("Invalid Email ID");
                                     }
                                 }
                             }
        );

        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pw, hint, str = "*";
                String mail = t1.getText().toString().trim();
                //       user =db.getData(mail);
                user = db.getData(mail);
                if (user == null) {
                    AlertDialog ad = new AlertDialog.Builder(
                            LoginActivity.this).create();
                    ad.setTitle("Error");
                    ad.setMessage("No account under this email id.");
                    ad.setIcon(R.drawable.user_error);
                    ad.show();
                } else {
                    AlertDialog ad = new AlertDialog.Builder(
                            LoginActivity.this).create();
                    ad.setTitle("Hint");
                    pw = user.getPAss();
                    int l = pw.length();
                    hint = pw.substring(0, 2);
                    String s = new String(new char[l - 4]).replace("\0", "*");
                    hint = hint + s + pw.substring(l - 2, l);
                    ad.setMessage("Password Hint :" + hint);
                    ad.setIcon(R.drawable.hint);
                    ad.show();
                }
            }
        });
    }

    protected boolean isEmailValid(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(LOG_TAG, "onPause");
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "onDestroy");
    }


}
