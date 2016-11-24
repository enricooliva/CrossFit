package com.enricooliva.crossfit;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.enricooliva.crossfit.data.Athlete;
import com.enricooliva.crossfit.data.DataContract;


/**
 * Created by Enrico on 05/04/2015.
 */

public class AthleteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ATHLETE_LOADER = 0;
    private static final String LOG_TAG = "AthleteFragment";
    private ListView athleteListView;

    private AthleteAdapter mAthleteAdapter;

    private static final String[] ATHLETE_COLUMN = {
            DataContract.AthleteEntry.TABLE_NAME + "." + DataContract.AthleteEntry._ID,
            DataContract.AthleteEntry.COLUMN_id,
            DataContract.AthleteEntry.COLUMN_firstName,
            DataContract.AthleteEntry.COLUMN_lastName,
            DataContract.AthleteEntry.COLUMN_dateOfBirth,
            DataContract.AthleteEntry.COLUMN_sex,
            DataContract.AthleteEntry.COLUMN_email
    };

    public static final int COL_PLAYER_ID = 0;
    public static final int COL_PLAYER_NAME = 1;
    public static final int COL_PLAYER_NUMBER = 2;
    public static final int COL_PLAYER_RATE = 3;
    public static final int COL_PLAYER_ROLE = 4;
    public static final int COL_PLAYER_NUMBER_VOTE = 5;

    public AthleteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAthleteAdapter = new AthleteAdapter(getActivity(), null, 0);
        View rootView = inflater.inflate(R.layout.fragment_athlete, container, false);


        //athleteListView = getListView();
        // Get a reference to the ListView, and attach this adapter to it.
        athleteListView = (ListView) rootView.findViewById(R.id.listPlayer);

        //new Utility(this).execute(new String[]{"http://www.enricooliva.com/getAllPlayers.php"});

        athleteListView.setAdapter(mAthleteAdapter);

        athleteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = mAthleteAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    Athlete p = new Athlete(cursor);
                    LinearLayout layout = (LinearLayout) view;
                    //RatingBar ratingBar=(RatingBar) layout.findViewById(R.id.ratingBar1);
                    //new Vote(p,ratingBar,getActivity()).showvote();
                }
            }
        });

        return rootView;
    }

//    private void updateAthlete() {
//
//        new FetchAthleteTask(getActivity()).execute("");
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(ATHLETE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        //updateAthlete();
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(ATHLETE_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                DataContract.AthleteEntry.CONTENT_URI,
                ATHLETE_COLUMN,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");
        mAthleteAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAthleteAdapter.swapCursor(null);
    }


}
