package com.enricooliva.crossfit;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.enricooliva.crossfit.sync.SyncAdapter;
import com.enricooliva.crossfit.sync.SyncUtils;

public class MainActivity extends AppCompatActivity {


    public static final String AUTHORITY = "com.enricooliva.crossfit.datasync.provider";
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

        SyncAdapter.initializeSyncAdapter(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.LessonFragmentPlaceholder, new LessonFragment())
                    .commit();
        }

        mAccount = SyncUtils.CreateSyncAccount(this);
        SyncUtils.TriggerRefresh();
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
