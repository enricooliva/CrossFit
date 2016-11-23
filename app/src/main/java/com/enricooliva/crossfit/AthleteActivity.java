package com.enricooliva.crossfit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class AthleteActivity extends AppCompatActivity {

    private static final int HOME_ID = 0;
    private static final int ATHLETE_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athlete);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AthleteFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_athlete, menu);
        menu.add(Menu.NONE, HOME_ID, Menu.NONE, R.string.home);
        //menu.add(Menu.NONE,GAMES_ID, Menu.NONE, R.string.menuitem_games);
        //menu.add(Menu.NONE,TEAM_ID, Menu.NONE, R.string.menuitem_team);

        //menu.add(Menu.NONE,INFO, Menu.NONE, R.string.menuitem_info);
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
            case HOME_ID:
                Intent splash = new Intent(AthleteActivity.this, MainActivity.class);
                startActivity(splash);
                return true;

            case ATHLETE_ID:
                Intent gamelist = new Intent(AthleteActivity.this, AthleteActivity.class);
                startActivity(gamelist);
                return true;

        }

        return super.onOptionsItemSelected(item);

    }


}
