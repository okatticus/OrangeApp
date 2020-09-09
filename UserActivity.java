package com.example.android.orange;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.android.orange.Database.Db;

import java.util.Locale;

/**
 * Created by Apoorva on 6/3/2018.
 */

public class UserActivity extends AppCompatActivity implements QrFragment.OnFragmentInteractionListener, AdminFragment.OnFragmentInteractionListener,
        ScanFragment.OnFragmentInteractionListener, StatFragment.OnFragmentInteractionListener, UserFragment.OnFragmentInteractionListener, AllStatsFragment.OnFragmentInteractionListener {
    public String u, s, x, l;
    TextView usertxt;
    DrawerLayout mDrawerLayout;
    Toolbar toolbar;
    NavigationView mNavigationView;
    FragmentManager ft;
    QrFragment qrFragment = null;
    private Locale locale = null;
    AlertDialog alertDialog1;
    CharSequence[] values = {"English ", " French"};
    public String jj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        changeLang();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_user);
        s = getIntent().getStringExtra("username");
        u = getIntent().getStringExtra("usertype");
        x = getIntent().getStringExtra("userId");
        l = getIntent().getStringExtra("lastName");
        final int uId = Integer.parseInt(x);
        Log.v("UserActivity", x);
        toolbar = (
                Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_name);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view_admin);
        //Main fragment
        UserFragment uf = UserFragment.newInstance(s, l);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        ft = getSupportFragmentManager();
        boolean fragmentPopped = ft
                .popBackStackImmediate("main" +
                        "F", 0);
        if (!fragmentPopped && ft.findFragmentByTag("mainF") == null) {
            FragmentTransaction ftx = ft.beginTransaction();
            ftx.replace(R.id.fragmentParentViewGroup, uf, uf.getClass().getSimpleName()).commit();
        }

        //get Database
        Db db = new Db(this);
        if (u.equals("Admin")) {
            mNavigationView.getMenu().setGroupVisible(R.id.menubar, true);
            mNavigationView.getMenu().setGroupVisible(R.id.menuqr, true);
            mNavigationView.getMenu().setGroupVisible(R.id.menudel, true);
            mNavigationView.getMenu().setGroupVisible(R.id.menuScan, false);
            mNavigationView.getMenu().setGroupVisible(R.id.menuStats, false);
            mNavigationView.getMenu().setGroupVisible(R.id.userStatistics, true);
            mNavigationView.getMenu().setGroupVisible(R.id.changeLang, true);
            mNavigationView.getMenu().setGroupVisible(R.id.menulog, true);

        } else if (u.equals("Privileged")) {
            mNavigationView.getMenu().setGroupVisible(R.id.menubar, true);
            mNavigationView.getMenu().setGroupVisible(R.id.menuqr, true);
            mNavigationView.getMenu().setGroupVisible(R.id.menudel, true);
            mNavigationView.getMenu().setGroupVisible(R.id.menuScan, true);
            mNavigationView.getMenu().setGroupVisible(R.id.menuStats, true);
            mNavigationView.getMenu().setGroupVisible(R.id.userStats, false);
            mNavigationView.getMenu().setGroupVisible(R.id.changeLang, false);
            mNavigationView.getMenu().setGroupVisible(R.id.menulog, true);

        } else if (u.equals("Other")) {
            mNavigationView.getMenu().setGroupVisible(R.id.menubar, true);
            mNavigationView.getMenu().setGroupVisible(R.id.menuqr, true);
            mNavigationView.getMenu().setGroupVisible(R.id.menudel, false);
            mNavigationView.getMenu().setGroupVisible(R.id.menuScan, true);
            mNavigationView.getMenu().setGroupVisible(R.id.menuStats, true);
            mNavigationView.getMenu().setGroupVisible(R.id.userStats, false);
            mNavigationView.getMenu().setGroupVisible(R.id.changeLang, false);
            mNavigationView.getMenu().setGroupVisible(R.id.menulog, true);

        }

        NavigationView nav = findViewById(R.id.nav_view_admin);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Qr: {
                        Bundle bundle = new Bundle();
                        qrFragment = QrFragment.newInstance();
                        bundle.putString("type", "qr");
                        bundle.putString("userId", x);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        qrFragment.setArguments(bundle);
                        ft = getSupportFragmentManager();
                        boolean fragmentPopped = ft
                                .popBackStackImmediate("qr", 0);
                        if (!fragmentPopped && ft.findFragmentByTag("qr") == null) {
                            FragmentTransaction ftx = ft.beginTransaction();
                            ftx.replace(R.id.fragmentParentViewGroup, qrFragment, qrFragment.getClass().getSimpleName()).addToBackStack("qr").commit();
                        }
                        return true;
                    }
                    case R.id.Bar: {
                        Bundle bundle = new Bundle();
                        qrFragment = QrFragment.newInstance();
                        bundle.putString("type", "bar");
                        bundle.putString("userId", x);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        qrFragment.setArguments(bundle);
                        ft = getSupportFragmentManager();
                        boolean fragmentPopped = ft
                                .popBackStackImmediate("bar", 0);
                        if (!fragmentPopped && ft.findFragmentByTag("bar") == null) {
                            FragmentTransaction ftx = ft.beginTransaction();
                            ftx.replace(R.id.fragmentParentViewGroup, qrFragment, qrFragment.getClass().getSimpleName()).addToBackStack("bar").commit();
                        }
                        return true;
                    }
                    case R.id.Delete: {
                        AdminFragment af = AdminFragment.newInstance(u, uId);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        ft = getSupportFragmentManager();
                        boolean fragmentPopped = ft
                                .popBackStackImmediate("admin", 0);
                        if (!fragmentPopped && ft.findFragmentByTag("admin") == null) {
                            FragmentTransaction ftx = ft.beginTransaction();
                            ftx.replace(R.id.fragmentParentViewGroup, af, af.getClass().getSimpleName()).addToBackStack("admin").commit();
                        }
                        return true;
                    }
                    case R.id.Scans: {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        ScanFragment sf = ScanFragment.newInstance(uId);
                        ft = getSupportFragmentManager();
                        boolean fragmentPopped = ft.popBackStackImmediate("scans", 0);
                        if (!fragmentPopped && ft.findFragmentByTag("scans") == null) {
                            FragmentTransaction ftx = ft.beginTransaction();
                            ftx.replace(R.id.fragmentParentViewGroup, sf, sf.getClass().getSimpleName()).addToBackStack("scans").commit();
                        }
                        return true;
                    }
                    case R.id.Statistics: {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        StatFragment sf = StatFragment.newInstance(x);
                        ft = getSupportFragmentManager();
                        boolean fragmentPopped = ft.popBackStackImmediate("stats", 0);
                        if (!fragmentPopped && ft.findFragmentByTag("stats") == null) {
                            FragmentTransaction ftx = ft.beginTransaction();
                            ftx.replace(R.id.fragmentParentViewGroup, sf, sf.getClass().getSimpleName()).addToBackStack("stats").commit();
                        }
                        return true;
                    }
                    case R.id.userStatistics: {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        AllStatsFragment asf = AllStatsFragment.newInstance();
                        ft = getSupportFragmentManager();
                        boolean fragmentPopped = ft.popBackStackImmediate("all", 0);
                        if (!fragmentPopped && ft.findFragmentByTag("all") == null) {
                            FragmentTransaction ftx = ft.beginTransaction();
                            ftx.replace(R.id.fragmentParentViewGroup, asf, asf.getClass().getSimpleName()).addToBackStack("all").commit();
                        }
                        return true;
                    }
                    case R.id.Lang: {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        CreateLanguageAlertDialog();
                        return true;
                    }
                    case R.id.logOut: {
                        Intent i = new Intent(UserActivity.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void CreateLanguageAlertDialog() {

        final AlertDialog builder = new AlertDialog.Builder(this).create();
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.radio_lang, null);
        builder.setView(view);
        Configuration config = getBaseContext().getResources().getConfiguration();
        final SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(this).edit();
        String L = "en";
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        final RadioGroup rg = (RadioGroup) view.findViewById(R.id.radio_grp);
        L = sp.getString(getString(R.string.locale_lang), "en");
        final String finalL = L;
        final RadioButton bt1 = (RadioButton) view.findViewById(R.id.en);
        final RadioButton bt2 = (RadioButton) view.findViewById(R.id.fr);
        if (L.equals("en")) {
            rg.check(bt1.getId());
        } else {
            rg.check(bt2.getId());
        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == bt1.getId()) {
                    rg.check(bt1.getId());
                    Log.v("Button 1id", String.valueOf(bt1.getId()));
                    if (finalL.equals("en")) {
                        AlertDialog ad = new AlertDialog.Builder(UserActivity.this).create();
                        Resources res = getResources();
                        String text = String.format(res.getString(R.string.change_Lan));
                        ad.setTitle(text);
                        ad.setMessage("Already the same langugage");
                        ad.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // dialog.dismiss();
                                    }
                                }
                        );
                        ad.show();
                    } else {
                        AlertDialog.Builder builder1= new AlertDialog.Builder(UserActivity.this);

                        final AlertDialog ad = builder1.create();
                       // AlertDialog ad = new AlertDialog.Builder(UserActivity.this).create();
                        Resources res = getResources();
                        String text = String.format(res.getString(R.string.change_Lan));
                        ad.setTitle(text);
                        ad.setMessage(res.getString(R.string.restarting));
                        ad.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ed.putString(getString(R.string.locale_lang), "en");
                                ed.commit();
                                changeLang();
                                Intent intent = new Intent(UserActivity.this, Splash.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("EXIT", true);
                                startActivity(intent);
                            }
                        });
                        ad.setButton(Dialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               ad.setCanceledOnTouchOutside(false);
                                builder.dismiss();
                            }
                        });
                        ad.show();
                    }
                } else if (i == bt2.getId()) {
                    rg.check(bt2.getId());
                    Log.v("Button 2id", String.valueOf(bt2.getId()));
                    Log.v("UserActivity L", finalL);
                    if (finalL.equals("fr")) {
                        AlertDialog ad = new AlertDialog.Builder(UserActivity.this).create();
                        Resources res = getResources();
                        String text = String.format(res.getString(R.string.change_Lan));
                        ad.setTitle(text);
                        ad.setMessage("Already the same langugage");
                        ad.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                          builder.dismiss();
                                    }
                                }
                        );
                        ad.show();
                    } else {

                        AlertDialog.Builder builder1= new AlertDialog.Builder(UserActivity.this);

                       final AlertDialog ad = builder1.create();
                      //  AlertDialog ad = new AlertDialog.Builder(UserActivity.this).create();
                        Resources res = getResources();
                        String text = String.format(res.getString(R.string.change_Lan));
                        ad.setTitle(text);
                        ad.setMessage(res.getString(R.string.restarting));
                        ad.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ed.putString(getString(R.string.locale_lang), "fr");
                                ed.commit();
                                changeLang();
                                Intent intent = new Intent(UserActivity.this, Splash.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("EXIT", true);
                                startActivity(intent);
                            }
                        });
                        ad.setButton(Dialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                builder.dismiss();
                            }
                        });
                        ad.setCanceledOnTouchOutside(false);
                        ad.show();
                    }
                }
            }
        });
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog ad = new AlertDialog.Builder(UserActivity.this).create();
                Resources res = getResources();
                String text = String.format(res.getString(R.string.change_Lan));
                ad.setTitle(text);
                ad.setMessage("Already the same langugage");
                ad.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                builder.dismiss();
                            }
                        }
                );
                ad.show();
                builder.dismiss();
            }
        });

        builder.show();
    }

    /*

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Your Choice");
            //builder.setContentView();
            Configuration config = getBaseContext().getResources().getConfiguration();
            final SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(this).edit();
            String L = "en";
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

            L = sp.getString(getString(R.string.locale_lang), "en");
            final String finalL = L;
            builder.setSingleChoiceItems(values, Dialog.BUTTON_POSITIVE, new DialogInterface.OnClickListener() {
                String x;

                public void onClick(DialogInterface dialog, int item) {
                    switch (item) {
                        case 0:
                            x = "en";
                            ed.putString(getString(R.string.locale_lang), "en");

                            ed.commit();
                            break;
                        case 1:
                            x = "fr";
                            ed.putString(getString(R.string.locale_lang), "fr");
                            ed.commit();
                            break;
                    }
                    Log.v("UserActivity L", finalL);
                    Log.v("UserActivity x", x);
                    if (finalL.equals(x)) {
                        AlertDialog ad = new AlertDialog.Builder(UserActivity.this).create();
                        Resources res = getResources();
                        String text = String.format(res.getString(R.string.change_Lan));
                        ad.setTitle(text);
                        ad.setMessage("Already the same langugage");
                        ad.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        alertDialog1.dismiss();
                                    }
                                }
                        );
                        ad.show();
                    } else {
                        changeLang();
                        AlertDialog ad = new AlertDialog.Builder(UserActivity.this).create();
                        Resources res = getResources();
                        String text = String.format(res.getString(R.string.change_Lan));
                        ad.setTitle(text);
                        ad.setMessage(res.getString(R.string.restarting));
                        ad.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alertDialog1.dismiss();
                                Intent intent = new Intent(UserActivity.this, Splash.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("EXIT", true);
                                startActivity(intent);
                            }
                        });
                        ad.show();
                    }
                }
            });
            alertDialog1 = builder.create();
            alertDialog1.show();


      */
    @Override
    public void onAdminFragmentInteraction(String string) {
        // Do different stuff
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                usertxt = (TextView) (findViewById(R.id.username));
                usertxt.setText("Hi, " + s);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
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

