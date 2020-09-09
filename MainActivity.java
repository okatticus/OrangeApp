package com.example.android.orange;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.content.res.Configuration;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button b1, b2;
    private Locale locale = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set content view AFTER ABOVE sequence (to avoid crash)
        changeLang();
        setContentView(R.layout.activity_main);
        b1 = (Button) findViewById(R.id.Login);
        b2 = (Button) findViewById(R.id.SignUp);
        b1.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View view) {
                                      Intent i = new Intent(MainActivity.this, LoginActivity.class);
                                      i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                      startActivity(i);
                                  }
                              }
        );
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SignupActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
            }
        });

    }
    public void changeLang() {
        Configuration config = getApplicationContext().getResources().getConfiguration();
        final SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(this).edit();
        String lang = getLang();
        if (!"".equals(lang) && !config.locale.getLanguage().equals(lang)) {

            locale = new Locale(lang);
            Locale.setDefault(locale);
            Configuration conf = new Configuration(config);
            conf.locale = locale;
            getBaseContext().getResources().updateConfiguration(conf, getBaseContext().getResources().getDisplayMetrics());
        }
    }
    public String getLang() {
        return PreferenceManager.getDefaultSharedPreferences(this).getString(this.getString(R.string.locale_lang), "");
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (locale != null) {
            Locale.setDefault(locale);
            Configuration config = new Configuration(newConfig);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
    }


}
