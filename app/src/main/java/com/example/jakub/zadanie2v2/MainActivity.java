package com.example.jakub.zadanie2v2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView email;
    TextView topic;
    TextView content;
    Button sendingButton;
    public static final String PACKAGE_NAME = "com.google.android.gm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onNewIntent(getIntent());

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sendingButton = (Button) findViewById(R.id.send_Button);
        initializeComponents();

        sendingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        fab.setVisibility(View.INVISIBLE);
    }


    public boolean sendEmail() {

        if(!isValidEmailAddress(String.valueOf(email.getText()))) {
            Toast.makeText(MainActivity.this, "Email address is not valid", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!(String.valueOf(topic.getText())).trim().equals("")) {
            new android.app.AlertDialog.Builder(MainActivity.this).setMessage("Do you want to send this email without subject?")
                    .setNegativeButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            exportData();
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
            return true;
        }

        if(!(String.valueOf(email.getText()).trim().equals(""))) {

            new android.app.AlertDialog.Builder(MainActivity.this).setMessage("Do you want to send this email?")
                    .setNegativeButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            exportData();
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
            return true;
        }

        else
            Toast.makeText(MainActivity.this, "No email address found", Toast.LENGTH_SHORT).show();
        return false;
    }


    public void exportData(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{String.valueOf(email.getText())});
        intent.putExtra(Intent.EXTRA_SUBJECT, String.valueOf(topic.getText()));
        intent.putExtra(Intent.EXTRA_TEXT, String.valueOf(content.getText()));

        ResolveInfo gmail = new ResolveInfo();

        PackageManager pm = getPackageManager();
        List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);

        for (ResolveInfo app : apps)
            if (pm.getLaunchIntentForPackage(app.activityInfo.packageName) != null)
                gmail = app;

        intent.setClassName(PACKAGE_NAME ,gmail.activityInfo.name);
        startActivity(intent);
    }



    public void initializeComponents() {
        email = (TextView) findViewById(R.id.email_EditText);
        topic = (TextView) findViewById(R.id.topic_EditText);
        content = (TextView) findViewById(R.id.mailContent_EditText);

    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
