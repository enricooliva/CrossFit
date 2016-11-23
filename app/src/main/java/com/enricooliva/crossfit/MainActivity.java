package com.enricooliva.crossfit;

import android.accounts.Account;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;

import com.enricooliva.crossfit.sync.SyncUtils;

public class MainActivity extends AppCompatActivity {


    public static final String AUTHORITY = "com.enricooliva.crossfit.app";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "com.enricooliva.crossfit.account";
    // The account name
    public static final String ACCOUNT = "dummyaccount";

    private static final int ATHLETE_ID = 1;

    private boolean mTwoPane;
    private Account mAccount;

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*new Handler().postDelayed(new Runnable() {

            *//*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             *//*

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(MainActivity.this, AthleteActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);*/

        // Create account, if needed
        //mAccount = CreateSyncAccount(this);
        //mAccount = CreateSyncAccount(this);
        SyncUtils.TriggerRefresh();
        //refresh();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);

        menu.add(Menu.NONE, ATHLETE_ID, Menu.NONE, R.string.menuitem_athlete);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        switch (item.getItemId()) {
            case ATHLETE_ID:
                showAthleteList();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }


    private void showAthleteList() {
        //aprire activity list
        Intent athletelist = new Intent(this, AthleteActivity.class);
        startActivity(athletelist);
    }
}
