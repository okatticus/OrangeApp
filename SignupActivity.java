package com.example.android.orange;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.orange.Database.Db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Apoorva on 5/29/2018.
 */

public class SignupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public String selectedId;
    int flag = 0;
    ArrayAdapter<CharSequence> aa;
    User user;
    long adminId= 0;
    int year, month, day;
    String name, last, email, password, confirm, dob;
    private TextView dateView;
    private Calendar c;
    private Db db;
    private Locale locale = null;
    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);
        final EditText n = findViewById(R.id.name);
        final EditText l = findViewById(R.id.lastname);
        final EditText e = findViewById(R.id.emailid);;
        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == n.getId()) {
                    n.setCursorVisible(true);
                }
            }
        });

        db = new Db(getBaseContext());
        //user = db.getData("admin@lti.com");
        ArrayList<String> el = db.getAllElements();
        if (el.size() < 1) {
            flag = 0;
        } else {
            flag = 1;
        }
        //Setting a date picker
        dateView = (TextView) findViewById(R.id.date);
        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(dateView);
            }
        });
        Button b = findViewById(R.id.signup);
        Spinner s = findViewById(R.id.user_spinner);
        if (flag == 1) {
            aa =
                    ArrayAdapter.createFromResource(getBaseContext(), R.array.other_array, R.layout.white_spinner_item);
        } else if (flag == 0) {
            aa =
                    ArrayAdapter.createFromResource(getBaseContext(), R.array.user_array, R.layout.white_spinner_item);
        }
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(aa);
        s.setOnItemSelectedListener(this);
        b.setOnClickListener(new View.OnClickListener() {
            EditText p1 = findViewById(R.id.pass);
            EditText p2 = findViewById(R.id.cptext);

            @Override
            public void onClick(View view) {
                name = n.getText().toString().trim();
                last = l.getText().toString().trim();
                email = e.getText().toString().trim();
                password = p1.getText().toString();
                confirm = p2.getText().toString();
                dob = dateView.getText().toString();
                if (validateFields()) {
                    ArrayList<String> el = db.getAllElements();
                    if (el.size() < 1) {
                      //  db.setIncrement();
                        String userType = selectedId;
                        if(userType.equals("Other") || userType.equals("Privileged"))
                        { AlertDialog ad = new AlertDialog.Builder(SignupActivity.this).create();
                            ad.setTitle("Type");
                            ad.setMessage("Select user type -Admin");
                            ad.setIcon(R.drawable.user_error);
                            ad.show();
                        }
                        else{
                        flag = 1;
                        long id = db.enterData(name, last, email, password, userType, dob);
                        if (id <= 0) {
                            AlertDialog ad = new AlertDialog.Builder(SignupActivity.this).create();
                            ad.setTitle("Error");
                            ad.setIcon(R.drawable.user_error);
                            ad.show();
                        } else {
                           // Toast.makeText(getParent(),String.valueOf(id),Toast.LENGTH_SHORT).show();
                            user = db.getData(email);
                            long userId = user.getUserId();
                            String uu = user.getName();
                            Log.v("SignupActivity.java", uu + " saved");
                            Intent i = new Intent(SignupActivity.this, UserActivity.class);
                            i.putExtra("username", name);
                            i.putExtra("usertype", selectedId);
                            i.putExtra("userId", String.valueOf(userId));
                            i.putExtra("lastName", last);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(i);
                        }}
                    } else {
                        String userType = selectedId;
                        if (selectedId.equals("Admin")) {
                            Toast.makeText(getBaseContext(), "Select a User type of 'Other' ", Toast.LENGTH_SHORT).show();
                        } else {
                            long id = db.enterData(name, last, email, password, userType, dob);
                            if (id <= 0) {
                                AlertDialog ad = new AlertDialog.Builder(SignupActivity.this).create();
                                ad.setTitle("Error");
                                ad.setMessage("There is already an account under this Email ID.");
                                ad.setIcon(R.drawable.user_error);
                                ad.show();
                            } else {
                                user = db.getData(email);
                                long userId = user.getUserId();
                                String uu = user.getName();
                                Log.v("SignupActivity.java", uu + " saved");
                                Intent i = new Intent(SignupActivity.this, UserActivity.class);
                                i.putExtra("username", name);
                                i.putExtra("usertype", selectedId);
                                i.putExtra("lastName", last);
                                i.putExtra("userId", String.valueOf(userId));
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(i);
                            }
                        }//selected Id is not Admin
                    }//else for entering data and starting user activity end
                }//if validate fields ends
            }
        });
    }

    private boolean validateFields() {
        EditText e = findViewById(R.id.emailid);
        if (name.equals("") || last.equals("")) {
            Toast.makeText(getBaseContext(), "Please fill the name field.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.equals("")) {
            Toast.makeText(getBaseContext(), "Please fill the password field.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (confirm.equals("")) {
            Toast.makeText(getBaseContext(), "Please fill the confirm password field.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (dob.equals("")) {
            Toast.makeText(getBaseContext(), "Please provide your date of birth.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isEmailValid(email) || !isEmailUnique(email)) {
            AlertDialog ad = new AlertDialog.Builder(this).create();
            ad.setTitle("Incorrect Email");
            ad.setMessage("The email is either invalid or already in use.");
            ad.setIcon(R.drawable.user_error);
            ad.show();
            return false;
        } else if (!isPassValid(password)) {
            AlertDialog ad = new AlertDialog.Builder(SignupActivity.this).create();
            ad.setTitle("Error");
            ad.setMessage("Password should be atleast 8 characters long with a mix of numbers, captial and small letters.");
            ad.setIcon(R.drawable.hint);
            ad.show();
            e.setError(null);
            return false;
        }else  if (!confirm.equals(password)) {AlertDialog ad = new AlertDialog.Builder(SignupActivity.this).create();
            ad.setTitle("Password error");
            ad.setMessage("The passwords don't match.");
            ad.show();
            return false;}
        return true;
    }

    /* private Boolean checkForAdmin(String email, String pass) {
         if (!email.equals("admin@lti.com") || !pass.equals("Admin123")) {
             Toast.makeText(getBaseContext(), "Incorrect Admin credentials", Toast.LENGTH_SHORT).show();
             return false;
         }
         if (!selectedId.equals("Admin")) {
             Toast.makeText(getBaseContext(), "Select Admin as usertype", Toast.LENGTH_SHORT).show();
             return false;
         }
         return true;
     }
 */
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999); // On calling showDialog, the method onCreateDialog gets called automatically.
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            DatePickerDialog dp = new DatePickerDialog(this,
                    myDateListener, year, month, day);
            dp.getDatePicker().setMaxDate(System.currentTimeMillis());
            return dp;
        }
        return null;
    }

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        selectedId = parent.getItemAtPosition(pos).toString();
        Log.v("User type ", selectedId);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
        selectedId = null;
    }

    protected boolean isEmailValid(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
/*    public static boolean isEmailValid(String email)
    {
        String emailRegex =  "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }*/
    protected boolean isEmailUnique(CharSequence email) {
        return db.checkMail(email);
    }

    protected boolean isPassValid(CharSequence pass) {
        String exp = "((?=.*\\d)(?=.*[A-Z])(?=.*[@#$%]?)(?=.*[a-z]).{8,50})";
        Pattern pattern = Pattern.compile(exp);
        Matcher matcher = pattern.matcher(pass);
        return matcher.matches();
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
