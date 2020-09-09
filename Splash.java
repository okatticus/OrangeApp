package com.example.android.orange;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by Apoorva on 5/29/2018.
 */

public class Splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    Locale locale = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeLang();
        setContentView(R.layout.activity_splash);

        fadeAnim();
        if (getIntent().getBooleanExtra("EXIT", false)) {
            Intent intent = new Intent(this, Splash.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
            AlarmManager mgr = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 10, pendingIntent);
            finish();
            System.exit(2);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //After splash timeout, this message will run
                Intent i = new Intent(Splash.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                overridePendingTransition(0, 0);
            }
        }, SPLASH_TIME_OUT);
    }

    public void fadeAnim() {
        ImageView image = (ImageView) findViewById(R.id.back);
       // TextView welcome = findViewById(R.id.welcome);
        Animation fade =
                AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.fade);
        image.startAnimation(fade);
     //   welcome.startAnimation(fade);
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

    public String getLang() {
        return PreferenceManager.getDefaultSharedPreferences(this).getString(this.getString(R.string.locale_lang), "");
    }
}
